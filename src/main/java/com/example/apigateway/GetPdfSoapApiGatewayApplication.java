package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * PDF取得SOAP APIゲートウェイアプリケーションのメインクラス
 * 
 * <p>Spring Bootアプリケーションの起動と基本設定を管理します。</p>
 * 
 * <p>本アプリケーションは以下の主要機能を提供します:</p>
 * <ul>
 *   <li>Spring Bootアプリケーションとしての起動・設定管理</li>
 *   <li>REST通信のためのRestTemplateの提供</li>
 *   <li>SOAP APIゲートウェイ機能の基盤構築</li>
 * </ul>
 */
@SpringBootApplication // (1) Spring Bootの自動設定を有効化（コンポーネントスキャン、自動設定など）
public class GetPdfSoapApiGatewayApplication {

    /**
     * アプリケーションのエントリーポイント
     * 
     * @param args コマンドライン引数（現バージョンでは未使用）
     * 
     * <p>アプリケーション起動時に実行されるメインメソッドです。</p>
     * <p>以下の処理を自動的に実行します:</p>
     * <ol>
     *   <li>Springアプリケーションコンテキストの初期化</li>
     *   <li>内蔵Tomcatサーバーの起動（デフォルトポート:8080）</li>
     *   <li>コンポーネントスキャンの実行</li>
     *   <li>自動設定の適用</li>
     * </ol>
     */
    public static void main(String[] args) {
        // (2) Spring Bootアプリケーションを起動
        // - デフォルト設定で内蔵Tomcatサーバーを起動
        // - アプリケーションコンテキストを初期化
        // - @Component, @Service, @Repositoryなどのスキャン実行
        // - application.properties/ymlの設定を読み込み
        SpringApplication.run(GetPdfSoapApiGatewayApplication.class, args);
    }

    /**
     * RestTemplateのBean定義
     * 
     * <p>REST API呼び出し用のテンプレートを提供します。</p>
     * 
     * <p>本Beanは以下の用途で使用されます:</p>
     * <ul>
     *   <li>AWS API Gatewayとの通信</li>
     *   <li>外部SOAPサービスへのプロキシ通信</li>
     *   <li>PDFデータ取得用のHTTPリクエスト送信</li>
     * </ul>
     * 
     * @return 設定済みのRestTemplateインスタンス
     * 
     * @apiNote 本メソッドで生成されるRestTemplateは:
     * <ul>
     *   <li>デフォルトの接続タイムアウト設定を使用</li>
     *   <li>簡易的なエラーハンドリングを実装</li>
     *   <li>シングルトンとしてアプリケーション全体で共有</li>
     * </ul>
     */
    @Bean
    public RestTemplate restTemplate() {
        // (3) HTTP通信を行うためのRestTemplateを生成
        // - デフォルトの接続タイムアウト設定
        // - 簡易的なエラーハンドリング機能
        // - API Gatewayとの通信などに使用
        RestTemplate restTemplate = new RestTemplate();
        
        // 必要に応じてカスタム設定を追加可能
        // restTemplate.setErrorHandler(new CustomErrorHandler());
        // restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        
        return restTemplate;
    }
}