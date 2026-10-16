package com.example.note.feature.note;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "note")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Note {
    @Id // 主鍵
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "noteId") // 資料表欄位名稱
    private Long noteId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "created time", nullable = false, updatable = false) // 設定欄位不可為空，且不可更新
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z", timezone = "Asia/Taipei")
    // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Taipei")
    private LocalDateTime createdTime;

    @Column(name = "updated time", nullable = false)
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z", timezone = "Asia/Taipei")
    // @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Taipei")
    private LocalDateTime updatedTime;

    public Note(String title, String content, boolean status) {
        this.title = title;
        this.content = content;
        this.status = status;
    }

}
