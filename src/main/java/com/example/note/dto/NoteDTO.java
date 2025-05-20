package com.example.note.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

// 資料傳輸物件
@Data
public class NoteDTO {

    @Schema(description = "標題", example = "Title", required = true)
    @NotBlank(message = "缺少標題")
    @Size(min = 1, max = 200, message = "標題長度需介於 1~200 字元") // 限制字串長度
    private String title;

    @Schema(description = "內容", example = "Content", required = true)
    @NotBlank(message = "缺少內容")
    @Size(min = 1, max = 2000, message = "內容長度需介於 1~2000 字元") // 限制字串長度
    private String content;

    @Schema(description = "完成狀態，預設為False", example = "false", required = true)
    // @NotNull(message = "狀態不可為空")
    private boolean status = false;
}
