# Todo List API

## 專案簡介

這是一個基於 Spring Boot 建構的簡單代辦事項（Todo List）後端服務。專案提供一組 RESTful API，用於管理代辦事項的建立、讀取、更新和刪除 (CRUD) 操作。


專案的設計考量旨在提供一個清晰、高效且易於維護的基礎服務，主要特性和設計理念如下：

* **基於 Spring Boot：** 利用 Spring Boot 快速開發和部署 Spring 應用程式的特性，簡化了配置並提供了豐富的自動化功能。
* **RESTful API 設計：** 遵循 RESTful 原則設計 API 端點，提供清晰、標準化的介面供前端或其他服務呼叫。
* **資料庫：** 使用內嵌式的 **H2 Database**，非常適合開發和測試階段，無需額外安裝或配置外部資料庫服務，啟動後即可使用。
* **分層架構：** 採用經典的控制器 (Controller) -> 服務 (Service) -> 資料存取 (Repository) 分層設計，有效分離職責，提高了程式碼的可維護性、可擴展性和可測試性。
* **統一 API 回應格式：** 透過自定義的 `GenericAPIResponse` 類別，為所有 API 請求提供了一致的成功和錯誤回應結構，增強了前端處理邏輯的一致性。
* **全域異常處理：** 利用 `@RestControllerAdvice` 實現了全域異常處理機制，能夠集中捕獲和處理應用程式中拋出的各種異常，並回傳友善且結構化的錯誤訊息給客戶端。
* **API 文件：** 整合了 Springdoc OpenAPI (Swagger)，自動生成互動式的 API 文件。這使得 API 的探索、理解和測試變得更加直觀和高效。

## 使用技術

* Spring Boot
* Spring Data JPA
* H2 Database
* Maven
* Swagger (Springdoc OpenAPI)
* Lombok
* Jakarta Validation (用於資料驗證)

## 功能特色

* **代辦事項 CRUD 操作：** 提供完整的代辦事項建立 (`POST`)、讀取所有/單一代辦事項 (`GET`)、更新 (`PUT`) 和刪除 (`DELETE`) 等核心 API 功能。
* **分頁與排序：** 查詢所有代辦事項時，支援靈活的分頁 (`page`, `size`) 和根據指定欄位（如 `title`、`content`、`createdTime`、`updatedTime`）進行升序或降序 (`increment`) 排序。
* **條件搜尋：** 提供兩種搜尋模式：
    * 根據標題進行模糊搜尋 (`/search/title`)，支援不區分大小寫。
    * 根據標題和內容進行精確搜尋 (`/search/exact`)。
* **未完成事項篩選：** 專門提供了查詢所有未完成代辦事項的 API (`/search/notcompleted`)，同樣支援分頁和排序。
* **統一回應格式：** 所有 API 回應均採用 `GenericAPIResponse` 包裝，包含 `success` 狀態、`statusCode`、`message`、`time` (時間戳記)，以及實際的 `data` 或 `errorDetail`。
* **資料驗證：** 利用 Jakarta Validation 規範，對輸入的 `NoteDTO` 資料進行了嚴格的驗證（例如：`@NotBlank`、`@Size`），確保資料的有效性和完整性。
* **全域錯誤處理：** 程式透過 `GlobalExceptionHandler` 捕獲並處理多種常見錯誤，如請求參數驗證失敗 (`MethodArgumentNotValidException`) 或資源未找到 (`ResourceNotFoundException`)，並以統一的錯誤格式回應。
* **自動生成 API 文件：** 內建的 Swagger UI 提供了一個互動式介面，開發者可以方便地查看所有 API 端點的詳細資訊、參數、回應範例，並直接在介面中進行測試。

## 建置與執行

### 環境需求

* Java **21** 或更高版本
* Maven **3.9.9** 或更高版本

### 建置步驟

1.  Clone 此專案至本地：
    ```bash
    git clone https://github.com/maverickzheng/todo-list-api.git
    cd todo-list-api
    ```
2.  使用 Maven 建置專案：
    ```bash
    mvn clean install
    ```
    此命令會編譯程式碼、運行測試（如果有的話），並在 `target` 資料夾下生成一個可執行的 JAR 文件。

### 執行專案

專案建置成功後，可以透過以下命令執行 Spring Boot 應用程式：

```bash
java -jar target/note-0.0.1-SNAPSHOT.jar
```

專案啟動後，內嵌的 H2 Database 會自動初始化並開始運行，無需額外配置資料庫服務。

## API 文件

專案成功啟動後，可以在瀏覽器中訪問以下網址來查看由 Swagger UI 生成的互動式 API 文件：

http://localhost:8080/swagger-ui.html

透過此介面，可以探索所有可用的 API 端點、查看每個端點的詳細說明、預期參數和回應格式，並直接在瀏覽器中發送請求進行測試。

## API 端點

以下列出本服務主要提供的 API 端點概覽。更多詳細的參數說明和回應範例，請務必參考上述的 Swagger UI 文件。

* `GET /api/notes`：取得所有代辦事項 (支援分頁和排序)
* `GET /api/notes/{id}`：根據 ID 取得代辦事項
* `POST /api/notes`：新增代辦事項
* `PUT /api/notes/{id}`：更新指定ID的代辦事項
* `DELETE /api/notes/{id}`：刪除指定ID的代辦事項
* `GET /api/notes/search/notcompleted`：取得未完成代辦事項 (支援分頁排序)
* `GET /api/notes/search/title`：根據標題搜尋代辦事項 (支援分頁排序)
* `GET /api/notes/search/exact`：根據標題和內容精確搜尋代辦事項

## 未來規劃

* 將 H2 Database 替換為生產級資料庫 (如 MySQL、PostgreSQL) 的可能性。
* 整合 Spring Security，提供使用者認證 (Authentication) 和授權 (Authorization) 功能，以增強 API 的安全性。
* 考慮引入整合測試，提高程式碼品質與可靠性。
