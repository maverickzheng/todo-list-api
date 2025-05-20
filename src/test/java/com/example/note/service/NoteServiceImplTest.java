package com.example.note.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.note.exception.ResourceNotFoundException;
import com.example.note.model.Note;
import com.example.note.repository.NoteRepository;

/*
 * 參考資料：
 * Mockito @Mock與@InjectMocks的差別
 * https://matthung0807.blogspot.com/2018/08/mockito-mockinjectmocks.html
 * Java Unit Test — Mockito
 * https://vivifish.medium.com/java-%E5%96%AE%E5%85%83%E6%B8%AC%E8%A9%A6%E5%B7%A5%E5%85%B7-mockito-e5f0ce93579d
 * 「Spring Boot API 開發：從 0 到 1」Day 26 單元測試
 * https://blog.cashwu.com/blog/2024/ithome/30day-day26-spring-boot-unit-test
 * SpringBoot - 單元測試工具 Mockito
 * https://kucw.io/blog/2020/2/spring-unit-test-mockito/
 * 
 */

@ExtendWith(MockitoExtension.class)
public class NoteServiceImplTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    @Test
    @DisplayName("查詢存在的代辦事項")
    public void getNoteById_existingNote() {
        // Arrange
        Long noteId = 1L;
        String title = "Test Title";
        String content = "Test Content";
        boolean status = false;
        LocalDateTime now = LocalDateTime.now();
        Note expectedNote = new Note(noteId, title, content, status, now, now);
        when(noteRepository.findById(noteId)).thenReturn(Optional.of(expectedNote));

        // Act
        Note actualNote = noteService.getNoteById(noteId);

        // Assert
        assertNotNull(actualNote, "應該要有物件！");
        assertEquals(noteId, actualNote.getNoteId(), "ID不正確！");
        assertEquals(title, actualNote.getTitle(), "標題不一致！");
        assertEquals(content, actualNote.getContent(), "內容不一致！");
        assertEquals(status, actualNote.isStatus(), "狀態不正確！");
        assertEquals(now, actualNote.getCreatedTime(), "建立時間不一致！");
        assertEquals(now, actualNote.getUpdatedTime(), "更新時間不一致！");

    }

    @Test
    @DisplayName("查詢不存在的代辦事項")
    public void getNoteById_nonExistingNote() {
        // Arrange
        Long noteId = 1L;
        String expectedMessage = String.format("查無 ID 為 %d 的代辦事項！", noteId);
        when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            noteService.getNoteById(noteId);
        });
        assertEquals(expectedMessage, exception.getMessage(), "不正確的錯誤訊息！");
        verify(noteRepository, times(1)).findById(noteId);

    }

    @Test
    @DisplayName("更新不存在的代辦事項")
    public void updateNote_nonExistingNote() {
        // Arrange
        Long noteId = 1L;
        String expectedMessage = String.format("查無 ID 為 %d 的代辦事項！", noteId);
        when(noteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            noteService.getNoteById(noteId);
        });
        assertEquals(expectedMessage, exception.getMessage(), "不正確的錯誤訊息！");
        verify(noteRepository, times(1)).findById(noteId);
    }
}
