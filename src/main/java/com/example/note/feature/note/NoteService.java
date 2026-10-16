package com.example.note.feature.note;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.note.feature.note.NoteDTO;
import com.example.note.feature.note.Note;

/*
 * 參考資料：
 * Java 為什麼在Service層要使用Interface 
 * https://matthung0807.blogspot.com/2017/12/java-serviceinterface.html
 * 常見的 Java Interface 錯誤用法 
 * https://kaisheng714.github.io/articles/anti-pattern-of-java-interface-impl-style
 */
public interface NoteService {

    Page<Note> getAllNotes(Pageable pageable);

    Note getNoteById(Long id);

    Note createNote(NoteDTO noteDTO);

    // Note updateNote(Long id, Note updateNote);

    void updateNote(Long id, NoteDTO updateNote);

    void deleteNote(Long id);

    // 未完成待辦事項
    // List<Note> notCompleted(boolean order);

    Page<Note> searchNotCompleted(boolean increment, int page, String orderBy);

    // 按標題搜尋並排序
    // List<Note> searchNotesByTitle(String title, boolean order);

    Page<Note> searchNotesByTitle(String title, boolean order, int page, String orderBy);

    // 模糊搜尋並排序
    List<Note> searchExact(String title, String content, boolean order);

}
