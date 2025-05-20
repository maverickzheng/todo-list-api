package com.example.note.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.note.model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

        // 查詢未完成代辦事項
        Page<Note> findByStatusFalse(Pageable pageable);

        // 查詢未完成代辦事項，按建立時間遞增排序
        List<Note> findByStatusFalseOrderByCreatedTimeAsc();

        // 查詢未完成代辦事項，按建立時間遞減排序
        List<Note> findByStatusFalseOrderByCreatedTimeDesc();

        // 查詢符合部分 title 字詞，不分大小寫( Contains 和 Containing 無任何差異)
        // List<Note> findByTitleContainsIgnoreCase(String title);
        Page<Note> findByTitleContainsIgnoreCase(String title, Pageable pageable);

        // 查詢符合部分 title 字詞，不分大小寫( Contains 和 Containing 無任何差異)
        List<Note> findByTitleContainingIgnoreCase(String title);

        // 查詢符合部分 title 字詞，不分大小寫
        // 與 SQL 中 Like 的寫法一致
        List<Note> findByTitleLikeIgnoreCase(String title);

        // 查詢符合部分 title 和(And) content字詞、不分大小寫，按建立時間遞增排序
        List<Note> findByTitleContainsIgnoreCaseAndContentContainsIgnoreCaseOrderByCreatedTimeAsc(String title,
                        String content);

        // 查詢符合部分 title 和(And) content字詞、不分大小寫，按建立時間遞減排序
        List<Note> findByTitleContainsIgnoreCaseAndContentContainsIgnoreCaseOrderByCreatedTimeDesc(String title,
                        String content);

        // 查詢符合部分 content 字詞，按建立時間遞增
        List<Note> findByContentContainingIgnoreCaseOrderByCreatedTimeAsc(String content);

        // 查詢符合部分 content 字詞，按建立時間遞減
        List<Note> findByContentContainingIgnoreCaseOrderByCreatedTimeDesc(String content);

}
