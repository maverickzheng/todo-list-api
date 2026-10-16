package com.example.note.feature.note;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.note.feature.note.NoteDTO;
import com.example.note.common.exception.ResourceNotFoundException;
import com.example.note.feature.note.Note;
import com.example.note.feature.note.NoteRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NoteServiceImpl implements NoteService {

    /*
     * 參考資料：
     * 分析 Spring 的依賴注入模式：
     * https://kaisheng714.github.io/articles/analyzing-dependency-injection-
     * patterns-in-spring
     * 分析三種主要spring依賴注入的方式：
     * https://tingfengchen.medium.com/spring-autowired-recommend-aefd88570601
     */

    // Field Injection 欄位注入
    // @Autowired
    // private NoteRepository noteRepository;

    // Constructor Injection 建構子注入
    private final NoteRepository noteRepository;

    public NoteServiceImpl(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    Page<Note> notes;
    Pageable pageable;
    final int pageSize = 5;

    // 將Pageable的升降序依據模組化
    private Pageable buildPageable(int page, int pageSize, boolean increment, String orderBy) {
        if (increment) {
            return PageRequest.of(page, pageSize, Sort.Direction.ASC, orderBy);
        } else {
            return PageRequest.of(page, pageSize, Sort.Direction.DESC, orderBy);
        }
    }

    // 處理查詢結果
    private Page<Note> handleNotesResult(Page<Note> notes, String massage) {
        if (notes.isEmpty()) {
            throw new ResourceNotFoundException(massage);
        } else {
            return notes;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Note> getAllNotes(Pageable pageable) {
        Page<Note> notes = noteRepository.findAll(pageable);

        return notes;
    }

    @Override
    @Transactional(readOnly = true)
    public Note getNoteById(Long id) {
        return noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("查無 ID 為 %d 的待辦事項！", id)));
    }

    @Override
    @Transactional
    public Note createNote(NoteDTO noteDTO) {

        Note note = new Note();
        note.setTitle(noteDTO.getTitle());
        note.setContent(noteDTO.getContent());

        return noteRepository.save(note);

    }

    /*
     * @Override
     * 
     * @Transactional
     * public Note updateNote(Long id, Note renewNote) {
     * 
     * Note existingNote = noteRepository.findById(id).orElseThrow(() -> new
     * ResourceNotFoundException(
     * String.format("查無 ID 為 %d 的待辦事項，故無法更新！", id)));
     * 
     * if (renewNote.getTitle() != null) {
     * existingNote.setTitle(renewNote.getTitle());
     * }
     * if (renewNote.getDescription() != null) {
     * existingNote.setDescription(renewNote.getDescription());
     * }
     * if (Objects.nonNull(renewNote.isStatus())) {
     * existingNote.setStatus(renewNote.isStatus());
     * }
     * 
     * return noteRepository.save(existingNote);
     * }
     */

    @Override
    @Transactional
    public void updateNote(Long id, NoteDTO renewNoteDTO) {

        Note existingNote = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("查無 ID 為 %d 的待辦事項，故無法更新！", id)));

        if (renewNoteDTO.getTitle() != null) {
            existingNote.setTitle(renewNoteDTO.getTitle());
        }
        if (renewNoteDTO.getContent() != null) {
            existingNote.setContent(renewNoteDTO.getContent());
        }
        if (Objects.nonNull(renewNoteDTO.isStatus())) {
            existingNote.setStatus(renewNoteDTO.isStatus());
        }
        noteRepository.save(existingNote);
    }

    @Override
    @Transactional
    public void deleteNote(Long id) {
        noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("查無 ID 為 %d 的待辦事項，故無法刪除！", id)));

        noteRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Note> searchNotCompleted(boolean increment, int page, String orderBy) {

        pageable = buildPageable(page, pageSize, increment, orderBy);

        notes = noteRepository.findByStatusFalse(pageable);
        String massage = "查無任何未完成的待辦事項！";

        return handleNotesResult(notes, massage);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<Note> searchNotesByTitle(String title, boolean increment, int page, String orderBy) {

        pageable = buildPageable(page, pageSize, increment, orderBy);

        notes = noteRepository.findByTitleContainsIgnoreCase(title, pageable);

        String massage = String.format("查無任何標題有包含「 %s 」待辦事項！", title);
        return handleNotesResult(notes, massage);

    }

    @Override
    @Transactional(readOnly = true)
    public List<Note> searchExact(String title, String content, boolean increment) {

        List<Note> notes;

        if (increment) {
            notes = noteRepository.findByTitleContainsIgnoreCaseAndContentContainsIgnoreCaseOrderByCreatedTimeAsc(
                    title, content);
        } else {
            notes = noteRepository.findByTitleContainsIgnoreCaseAndContentContainsIgnoreCaseOrderByCreatedTimeDesc(
                    title, content);
        }

        if (notes.isEmpty()) {
            throw new ResourceNotFoundException(String.format("查無任何標題包含「%s」和內容包含「%s」待辦事項！", title, content));
        } else {
            return notes;
        }

    }
}
