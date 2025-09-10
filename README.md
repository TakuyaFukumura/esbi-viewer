# ESBI Viewer

[![Build](https://github.com/TakuyaFukumura/esbi-viewer/workflows/Build/badge.svg)](https://github.com/TakuyaFukumura/esbi-viewer/actions?query=branch%3Amain)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.6.3-blue)](https://maven.apache.org/)

ESBIクワドラント（従業員・自営業者・事業主・投資家）の収入構造を可視化するWebアプリケーション

## 概要

ESBI Viewerは、ESBIクワドラント理論を基にした収入構造可視化ツールです。

### ESBIクワドラントとは
- **E（Employee）**: 従業員 - 給与所得
- **S（Self-employed）**: 自営業者 - 事業所得
- **B（Business owner）**: 事業主 - 事業からの不労所得
- **I（Investor）**: 投資家 - 投資からの不労所得

### 主な機能
- **データ入力**: 各クワドラントの収入額を入力
- **可視化**: 収入構造をクワドラント形式で視覚的に表示
- **履歴管理**: 過去の収入データの記録と管理
- **ガイド**: ESBIクワドラントの概念解説

## Docker開発環境セットアップ

### 前提条件
- Docker
- Docker Compose

### 起動手順
1. リポジトリをクローン
    ```bash
    git clone https://github.com/TakuyaFukumura/esbi-viewer.git
    ```
    ```bash
    cd esbi-viewer
    ```
2. Docker Composeでアプリケーションを起動
    ```bash
    docker compose up --build
    ```
3. ブラウザでアクセス

    http://localhost:8080

4. アプリケーション機能
    - **ホーム**: ESBIクワドラントの概要表示
    - **データ入力**: `/esbi/input` - 収入データの入力
    - **可視化**: `/esbi/visualization` - クワドラント可視化
    - **履歴**: `/esbi/history` - 過去データの閲覧
    - **ガイド**: `/esbi/guide` - ESBIクワドラント解説

5. H2データベースコンソールへのアクセス（開発用）

    http://localhost:8080/h2-console

6. ヘルスチェックエンドポイント

    http://localhost:8080/actuator/health

### Docker コマンド

#### アプリケーションの停止
```bash
docker compose down
```

#### ログの確認
```bash
docker compose logs -f app
```

#### イメージの再ビルド
```bash
docker compose build --no-cache
```

## 従来の起動方法（Docker不使用）

### 起動
```bash
./mvnw spring-boot:run
```

### コンパイルと実行
```bash
./mvnw clean package
```
```bash
java -jar target/esbi-viewer.jar
```

## 静的解析ツール（SpotBugs）

### SpotBugsとは
SpotBugsは、Javaコードの潜在的なバグや問題を検出する静的解析ツールです。
コードのコンパイル後のバイトコードを解析し、一般的なバグパターンや問題のあるコーディングパターンを発見します。

### SpotBugsの実行

#### 基本的な解析の実行
```bash
./mvnw spotbugs:spotbugs
```

#### 解析結果の確認とビルド時のチェック
```bash
./mvnw spotbugs:check
```

#### HTMLレポートの確認
解析実行後、次のファイルでHTMLレポートを確認できます： target/site/spotbugs.html

各OSでのコマンド例:
- **Windows**:
    ```bash
    start target/site/spotbugs.html
    ```
- **macOS**:
    ```bash
    open target/site/spotbugs.html
    ```
- **Linux**:
    ```bash
    xdg-open target/site/spotbugs.html
    ```

### SpotBugsの設定

#### 解析対象の設定
- `spotbugs-include.xml`: 解析対象のパッケージやクラスを指定
- `spotbugs-exclude.xml`: 解析から除外するパッケージやクラスを指定

#### 解析レベルの設定
- **Effort**: Max（最大）- より詳細な解析を実行
- **Threshold**: Low（低）- より多くの問題を検出

### Docker環境での実行
Docker環境でもSpotBugsを実行できます：
```bash
docker compose exec app ./mvnw spotbugs:spotbugs
```

## アプリケーション固有の機能

### データ構造
- **EsbiData**: 各クワドラントの収入データを格納するエンティティ
- **ユーザー管理**: 現在はデフォルトユーザーでの単一ユーザー対応
- **履歴機能**: 入力データの時系列管理

### 技術仕様
- **フロントエンド**: Thymeleaf + Bootstrap 5
- **バックエンド**: Spring Boot 3.5.5 + Spring Data JPA
- **データベース**: H2（インメモリ）
- **ビルドツール**: Maven
- **テストフレームワーク**: Spock (Groovy)
