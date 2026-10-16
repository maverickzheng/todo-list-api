package com.example.note.common.exception;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
// import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.method.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.note.common.GenericAPIResponse;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;

/*
 * 在Springdoc V2.6.0下，同時使用 @RestControllerAdvice(@ControllerAdvice)和 Swagger的解決辦法：
 * https://springdoc.org/#how-can-i-hide-an-operation-or-a-controller-from-documentation
 */

// @Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

        /*
         * 取得請求 URI
         * 方法一：https://ithelp.ithome.com.tw/articles/10347713
         * 方法二：https://medium.com/@vino7tech/generic-apiresponse-and-global-exception-
         * handling-in-spring-boot-221ce807bca6
         */
        private static String requestPath() {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                                .currentRequestAttributes()).getRequest();
                return request.getRequestURI();
        }

        // 伺服器錯誤
        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 將HTTP代碼產生在說明文檔上
        public ResponseEntity<GenericAPIResponse<String>> handleException(Exception ex) {

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(GenericAPIResponse.error(500, "伺服器錯誤！", "哎呀！發生了一點狀況，工程師正在搶修中，請稍後！",
                                                null));
        }

        // 不存在的路徑
        @ExceptionHandler(NoResourceFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ResponseEntity<GenericAPIResponse<String>> handleNoResourceFoundException(NoResourceFoundException ex) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(GenericAPIResponse.error(404, "無此路徑！", ex.getMessage(), requestPath()));
        }

        // 請求方法錯誤
        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
        public ResponseEntity<GenericAPIResponse<String>> handleHttpRequestMethodNotSupportedException(
                        HttpRequestMethodNotSupportedException ex) {

                String method = ex.getMethod(); // 獲取當前請求的方法
                String supportedMethods = String.join(" , ", ex.getSupportedMethods()); // 獲取可支援的方法列表
                String message = String.format("請求方法 '%s' 不被支援，僅支援方法: [%s]", method, supportedMethods);

                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                                .body(GenericAPIResponse.error(405, "請求方法錯誤！",
                                                message, requestPath()));
        }

        // 查無資料
        @ExceptionHandler(ResourceNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ResponseEntity<GenericAPIResponse<String>> handleResourceNotFoundException(Exception ex) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(GenericAPIResponse.error(404, "查無資料！", ex.getMessage(), requestPath()));
        }

        // PathVariable 參數類型不匹配
        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<GenericAPIResponse<String>> handleMethodArgumentTypeMismatchException(
                        MethodArgumentTypeMismatchException ex) {

                // String variableName = ex.getParameter().getParameterName(); // 取得參數名稱，擇一使用
                String variableName = ex.getName(); // 取得參數名稱，擇一使用
                String type = ex.getRequiredType().getSimpleName();
                Object actualValue = ex.getValue(); // 取得使用者輸入的值

                String errorDetail = String.format("參數 '%s' 應為類型 '%s' 。您輸入的值是 '%s' ！",
                                variableName, type,
                                actualValue);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(GenericAPIResponse.error(400, "參數型態錯誤！", errorDetail, requestPath()));
        }

        // RequestBody 格式異常
        @ExceptionHandler(HttpMessageNotReadableException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<GenericAPIResponse<String>> handleHttpMessageNotReadableException(
                        HttpMessageNotReadableException ex) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(GenericAPIResponse.error(400, "無法解析請求內容！", ex.getMessage(), requestPath()));
        }

        // 資料驗證錯誤(格式不符或是不符規定)
        @ExceptionHandler(MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<GenericAPIResponse<List<String>>> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException ex) {

                List<String> errorDetail = new ArrayList<>();

                ex.getBindingResult().getFieldErrors().forEach(error -> {
                        String fieldName = error.getField();
                        String fieldErrorDetail = error.getDefaultMessage();
                        String fieldMassage = String.join(": ", fieldName, fieldErrorDetail);
                        errorDetail.add(fieldMassage);
                });

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(GenericAPIResponse.error(400, "資料類型不匹配或不符規定！",
                                                errorDetail, requestPath()));
        }

        // 參數驗證錯誤 修改?????
        @ExceptionHandler(HandlerMethodValidationException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ResponseEntity<GenericAPIResponse<Object>> handleHandlerMethodValidationException(
                        HandlerMethodValidationException ex) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(GenericAPIResponse.error(400, "參數不匹配或是異常！",
                                                ex.getDetailMessageArguments(), requestPath()));

        }

        // DataIntegrityViolationException
}
