package com.example.apigateway.controller;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.example.apigateway.dto.GetPdfResponse;

@Controller
@RequestMapping
public class ApiGatewayController {

	// application.propertiesからAWS API GatewayのURLを注入
	@Value("${aws.api.gateway.url}")
	private String awsApiGatewayUrl;

	// REST API呼び出し用のテンプレート
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * PDFダウンロード画面を表示する
	 * @return PDFダウンロード画面のテンプレート名
	 */
	@GetMapping("/getPdf_OP7")
	public String showPdfDownloadForm() {
		return "pdfDownload";
	}

	/**
	 * API Gateway経由でPDFを取得する
	 * @return PDFファイルを含むレスポンスエンティティ
	 */
	@GetMapping("/ApiGateway_OP7")
	@ResponseBody
	public ResponseEntity<InputStreamResource> pdfGateway() {
		try {
			// AWS API GatewayにGETリクエストを送信
			GetPdfResponse response = restTemplate.getForObject(
					awsApiGatewayUrl,
					GetPdfResponse.class);

			// 結果に応じてHTTPステータスを決定
			HttpStatus status = response == null || response.getContent() == null ? HttpStatus.NOT_FOUND
					: HttpStatus.OK;

			// レスポンスのステータスコードとボディをチェック
			if (status == HttpStatus.OK) {

				// レスポンスのバイト配列から入力ストリームを作成
				ByteArrayInputStream bis = new ByteArrayInputStream(response.getContent());
				InputStreamResource resource = new InputStreamResource(bis);

				// ファイル名をUTF-8でエンコード（スペースは%20に置換）
				String encodedFilename = URLEncoder.encode(response.getFileName(), "UTF-8")
						.replace("+", "%20");

				// レスポンスヘッダーを設定
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_PDF); // PDFコンテンツタイプ
				headers.setContentDisposition(
						ContentDisposition.attachment() // 添付ファイルとしてダウンロード
								.filename(encodedFilename) // エンコード済みファイル名
								.build());

				// 成功レスポンスを返す
				return ResponseEntity.ok()
						.headers(headers)
						.body(resource);
			} else {
				// PDFが存在しない場合のエラーメッセージ
				String errorMessage = "PDFファイルが存在しません";
				ByteArrayInputStream errorStream = new ByteArrayInputStream(
						errorMessage.getBytes(StandardCharsets.UTF_8));
				InputStreamResource resource = new InputStreamResource(errorStream);

				// 404エラーレスポンスを返す
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.contentType(MediaType.TEXT_PLAIN)
						.body(resource);
			}
		} catch (Exception e) {
			// 予期せぬエラーが発生した場合
			String errorMessage = "システムエラーが発生しました: " + e.getMessage();
			ByteArrayInputStream errorStream = new ByteArrayInputStream(
					errorMessage.getBytes(StandardCharsets.UTF_8));

			// 500エラーレスポンスを返す
			return ResponseEntity.internalServerError()
					.contentType(MediaType.TEXT_PLAIN)
					.body(new InputStreamResource(errorStream));
		}
	}
}

