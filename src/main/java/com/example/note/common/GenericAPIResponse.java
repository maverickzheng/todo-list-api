package com.example.note.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(hidden = true)
@Getter
@Setter
public class GenericAPIResponse<T> {

    private boolean success; // 是否成功

    private Integer statusCode; // HTTP狀態碼

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z", timezone = "Asia/Taipei") // 設定時間格式和時區
    private ZonedDateTime time = LocalDateTime.now().atZone(ZoneId.of("Asia/Taipei"));

    private String message; // 訊息

    private T errorDetail; // 詳細訊息

    private T data; // 回傳資料

    private String path; // 請求路徑

    public static <T> GenericAPIResponse<T> success(Integer statusCode, String message, T data) {
        GenericAPIResponse<T> response = new GenericAPIResponse<>();
        response.setSuccess(true);
        response.setStatusCode(statusCode);
        response.setMessage(message);
        response.setData(data);
        response.setErrorDetail(null);
        response.setPath(null);
        return response;
    }

    public static <T> GenericAPIResponse<T> error(Integer statusCode, String message, T errorDetail, String path) {
        GenericAPIResponse<T> response = new GenericAPIResponse<>();
        response.setSuccess(false);
        response.setStatusCode(statusCode);
        response.setMessage(message);
        response.setData(null);
        response.setErrorDetail(errorDetail);
        response.setPath(path);
        return response;
    }

}
