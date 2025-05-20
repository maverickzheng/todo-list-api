package com.example.note.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class NoteTest {

    @Test
    void testAllArgsConstructor() {
        // Arrange
        Long noteId = 1L;
        String title = "Test Title";
        String content = "Test Content";
        boolean status = true;
        LocalDateTime now = LocalDateTime.now();

        // Act
        Note note = new Note(noteId, title, content, status, now, now);

        // Assert
        assertEquals(noteId, note.getNoteId(), "序號不相符！");
        assertEquals(title, note.getTitle(), "標題不相符！");
        assertEquals(content, note.getContent(), "內容不相符！");
        assertTrue(note.isStatus());
        assertEquals(now, note.getCreatedTime(), "建立時間不相符！");
        assertEquals(now, note.getUpdatedTime(), "更新時間不相符！");
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        Note note = new Note();

        // Assert
        assertNull(note.getNoteId());
        assertNull(note.getTitle());
        assertNull(note.getContent());
        assertFalse(note.isStatus());
        assertNull(note.getCreatedTime());
        assertNull(note.getUpdatedTime());
    }

    @Test
    public void testConstructor() {
        // Arrange
        String title = "Constructor Title";
        String content = "Constructor Content";
        boolean status = true;

        // Act
        Note note = new Note(title, content, status);

        // Assert
        assertNull(note.getNoteId(), "序號不應該有值！");
        assertEquals(title, note.getTitle(), "標題不相符！");
        assertEquals(content, note.getContent(), "內容不相符！");
        assertEquals(status, note.isStatus(), "狀態不相符！");

        // createdTime 和 updatedTime 由 @CreatedDate 和 @LastModifiedDate 自動生成
        assertNull(note.getCreatedTime());
        assertNull(note.getUpdatedTime());
    }

    @Test
    public void testGetterSetter() {
        // Arrange
        Long noteId = 1L;
        String title = "GetterSetter Title";
        String content = "GetterSetter Content";
        boolean status = true;
        LocalDateTime now = LocalDateTime.now();

        // Act
        Note note = new Note();
        note.setNoteId(noteId);
        note.setTitle(title);
        note.setContent(content);
        note.setStatus(status);
        note.setCreatedTime(now.minusHours(1));
        note.setUpdatedTime(now);

        // Assert
        assertEquals(noteId, note.getNoteId(), "序號不相符！");
        assertEquals(title, note.getTitle(), "標題不相符！");
        assertEquals(content, note.getContent(), "內容不相符！");
        assertEquals(status, note.isStatus(), "狀態不相符！");
        assertEquals(now.minusHours(1), note.getCreatedTime(), "建立時間不相符！");
        assertEquals(now, note.getUpdatedTime(), "更新時間不相符！");
    }

    @Test
    public void testStatusDefaultValue() {
        Note note = new Note();
        assertFalse(note.isStatus(), "狀態不相符！預設為False！");
    }

}