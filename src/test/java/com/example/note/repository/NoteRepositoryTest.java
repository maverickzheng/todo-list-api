package com.example.note.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.data.domain.*;

import com.example.note.model.Note;

/*
 * 參考資料：
 * 使用 @DataJpaTest 測試 Spring Data JPA
 * https://evanchen76.medium.com/%E4%BD%BF%E7%94%A8-datajpatest-%E6%B8%AC%E8%A9%A6-spring-data-jpa-a8338bcf377a
 */

@DataJpaTest(properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.jpa.hibernate.ddl-auto=create-drop"
}, showSql = true)
public class NoteRepositoryTest {

        @Autowired
        private TestEntityManager entityManager;

        @Autowired
        public NoteRepository noteRepository;

        // 在每個測試之前先做初始化
        @BeforeEach
        public void setUp() {
                // 清空資料庫
                noteRepository.deleteAll();

                // 可以在這裡增加測試資料或其他設置
                List<Note> testData = Arrays.asList(
                                new Note("測試標題1", "測試內容1", true),
                                new Note("測試標題2", "測試內容2", true),
                                new Note("測試標題3", "測試內容3", false),
                                new Note("測試標題4", "測試內容4", false),
                                new Note("Test Title 5", "Test Description 5", true),
                                new Note("Test title 6", "Test description 6", true),
                                new Note("test Title 7", "test Description 7", false),
                                new Note("test title 8", "test description 8", false),
                                new Note("測試標題9", "Test Description 9", true),
                                new Note("測試標題10", "Test description 10", false),
                                new Note("Test Title 11", "測試內容11", true),
                                new Note("Test title 12", "測試內容12", false));

                noteRepository.saveAll(testData);
        }

        // @Test
        // public void testId() {
        // Optional<Note> note = noteRepository.findById(1L);
        // assertTrue(note.isPresent(), "應該要有物件！");
        // }

        // 自動填入建立時間和更新時間
        @Test
        public void testNoteIdandTimeAuditing() {

                noteRepository.deleteAll();
                Note note = new Note("Auditing", "auditing", false);

                entityManager.persistAndFlush(note);

                assertNotNull(note.getNoteId(), "ID不應為空！");
                assertNotNull(note.getCreatedTime(), "建立時間不應為空！");
                assertNotNull(note.getUpdatedTime(), "更新時間不應為空！");
        }

        @Test
        @DisplayName("查詢未完成代辦事項，按建立時間升序回傳Pageable")
        public void testFindByStatusFalse_shouldReturnOrderbyCreatetimeASC() {
                // Arrange
                int page = 0;
                int pageSize = 5;
                String sortBy = "createdTime";
                Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, sortBy);

                int expectedTotalElements = 6; // 預期查詢結果的大小
                int expectedEntries = 5; // 預期當頁的資料筆數
                int expectedTotalPages = 2; // 預期總頁數

                // Act
                Page<Note> noteResult = noteRepository.findByStatusFalse(pageable);
                List<Note> noteContent = noteResult.getContent();

                // Assert
                assertEquals(expectedTotalElements, noteResult.getTotalElements(), "查詢結果的大小不正確！");
                assertEquals(expectedTotalPages, noteResult.getTotalPages(), "總頁數不正確！");
                assertEquals(expectedEntries, noteContent.size(), "當前頁面資料筆數不正確");
                assertTrue(noteContent.stream().allMatch(note -> !note.isStatus()),
                                "查詢結果中有完成的代辦事項！");
                assertTrue(noteContent.get(0).getCreatedTime().isBefore(noteContent.get(4).getCreatedTime()),
                                "排序不正確！"); // 檢查排序是否正確
        }

        @Test
        @DisplayName("查詢未完成代辦事項，按標題降序回傳Pageable")
        public void testFindByStatusFalse_shouldReturnOrderbyTitleDESC() {
                // Arrange
                int page = 1;
                int pageSize = 5;
                String sortBy = "title";
                Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, sortBy);

                int expectedTotalElements = 6; // 預期查詢結果的大小
                int expectedEntries = 1; // 預期當頁的資料筆數
                int expectedTotalPages = 2; // 預期總頁數
                String expectedContent = "Test title 12";

                // Act
                Page<Note> noteResult = noteRepository.findByStatusFalse(pageable);
                List<Note> noteContent = noteResult.getContent();

                // Assert
                assertEquals(expectedTotalElements, noteResult.getTotalElements(), "查詢結果的大小不正確！");
                assertEquals(expectedTotalPages, noteResult.getTotalPages(), "總頁數不正確！");
                assertEquals(expectedEntries, noteContent.size(), "當前頁面資料筆數不正確");
                assertTrue(noteContent.stream().allMatch(note -> !note.isStatus()),
                                "查詢結果中有完成的代辦事項！");
                assertEquals(expectedContent, noteContent.get(0).getTitle(), "標題不一致！");
        }

        @Test
        @DisplayName("查詢未完成代辦事項，按建立時間升序回傳List")
        public void testFindByStatusFalseOrderByCreatedTimeAsc() {
                // Arrange
                int expectedSize = 6; // 預期結果的大小

                // Act
                List<Note> noteResult = noteRepository.findByStatusFalseOrderByCreatedTimeAsc();

                // Assert
                assertEquals(expectedSize, noteResult.size(), "查詢結果的大小不正確！");
                assertTrue(noteResult.stream().allMatch(note -> !note.isStatus()),
                                "查詢結果中有完成的代辦事項！");
                assertTrue(noteResult.get(0).getCreatedTime().isBefore(noteResult.get(5).getCreatedTime()),
                                "排序不正確！"); // 檢查排序是否正確

        }

        @Test
        @DisplayName("查詢未完成代辦事項，按建立時間降序回傳List")
        public void testFindByStatusFalseOrderByCreatedTimeDesc() {
                // Arrange
                int expectedSize = 6; // 預期結果的大小

                // Act
                List<Note> noteResult = noteRepository.findByStatusFalseOrderByCreatedTimeDesc();

                // Assert
                assertEquals(expectedSize, noteResult.size(), "查詢結果的大小不正確！");
                assertTrue(noteResult.stream().allMatch(note -> !note.isStatus()),
                                "查詢結果中有完成的代辦事項！");
                assertTrue(noteResult.get(0).getCreatedTime().isAfter(noteResult.get(5).getCreatedTime()),
                                "排序不正確！"); // 檢查排序是否正確
        }

        @Test
        @DisplayName("查詢符合部分 title 字詞，不分大小寫( Contains 和 Containing 無任何差異)，按建立時間降序回傳Pageable")
        public void testFindByTitleContainsIgnoreCase_shouldReturnOrderbyCreatetimeDESC() {
                // Arrange
                String title = "測試"; // 查詢符合部分 title 字詞，不分大小寫
                int page = 0;
                int pageSize = 5;
                String sortBy = "createdTime";
                Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, sortBy);

                int expectedTotalElements = 6; // 預期查詢結果的大小
                int expectedEntries = 5; // 預期當頁的資料筆數
                int expectedTotalPages = 2; // 預期總頁數
                String expectedTitle = "測試標題10";

                // Act
                Page<Note> noteResult = noteRepository.findByTitleContainsIgnoreCase(title, pageable);
                List<Note> noteContent = noteResult.getContent();

                // Assert
                assertEquals(expectedTotalElements, noteResult.getTotalElements(), "查詢結果的大小不正確！");
                assertEquals(expectedTotalPages, noteResult.getTotalPages(), "總頁數不正確！");
                assertEquals(expectedEntries, noteContent.size(), "當前頁面資料筆數不正確！");
                assertTrue(noteContent.get(0).getCreatedTime().isAfter(noteContent.get(4).getCreatedTime()),
                                "排序不正確！"); // 檢查排序是否正確
                assertEquals(expectedTitle, noteContent.get(0).getTitle(), "標題不一致！");
        }

        @Test
        @DisplayName("查詢符合部分 title 字詞，不分大小寫( Contains 和 Containing 無任何差異)，按標題升序回傳Pageable")
        public void testFindByTitleContainsIgnoreCase_shouldReturnOrderbyTitleASC() {
                // Arrange
                String title = "tit"; // 查詢符合部分 title 字詞，不分大小寫
                int page = 1;
                int pageSize = 5;
                String sortBy = "title";
                Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.ASC, sortBy);

                int expectedTotalElements = 6; // 預期查詢結果的大小
                int expectedEntries = 1; // 預期當頁的資料筆數
                int expectedTotalPages = 2; // 預期總頁數
                String expectedTitle = "test title 8";

                // Act
                Page<Note> noteResult = noteRepository.findByTitleContainsIgnoreCase(title, pageable);
                List<Note> noteContent = noteResult.getContent();

                // Assert
                assertEquals(expectedTotalElements, noteResult.getTotalElements(), "查詢結果的大小不正確！");
                assertEquals(expectedTotalPages, noteResult.getTotalPages(), "總頁數不正確！");
                assertEquals(expectedEntries, noteContent.size(), "當前頁面資料筆數不正確！");
                assertEquals(expectedTitle, noteContent.get(0).getTitle(), "標題不一致！");
        }

        @Test
        @DisplayName("查詢符合部分 title 字詞，不分大小寫( Contains 和 Containing 無任何差異)")
        public void testFindByTitleContainingIgnoreCase() {
                // Arrange
                int expectedSize = 6; // 預期結果的大小
                String title = "titl"; // 查詢符合部分 title 字詞，不分大小寫
                String expectedTitle = title.toLowerCase();

                // Act
                List<Note> noteResult = noteRepository.findByTitleContainingIgnoreCase(title);

                // Assert
                assertEquals(expectedSize, noteResult.size(), "查詢結果的大小不正確！");
                assertTrue(noteResult.get(0).getTitle().toLowerCase().contains(expectedTitle));
        }

        // 查詢符合部分 title 字詞，不分大小寫
        // 與 SQL 中 Like 的寫法一致
        @Test
        @DisplayName("查詢符合部分 title 字詞，不分大小寫(與 SQL 中 Like 的寫法一致)")
        public void testFindByTitleLikeIgnoreCase() {
                // Arrange
                int expectedSize = 6; // 預期結果的大小

                String title = "tes"; // 查詢符合部分 title 字詞，不分大小寫
                String sqlTitle = "%" + title + "%"; // SQL 中 Like 的寫法
                String expectedTitle = title.toLowerCase();

                // Act
                List<Note> noteResult = noteRepository.findByTitleLikeIgnoreCase(sqlTitle);

                // Assert
                assertEquals(expectedSize, noteResult.size(), "查詢結果的大小不正確！");
                assertTrue(noteResult.get(0).getTitle().toLowerCase().contains(expectedTitle));
        }

        @Test
        @DisplayName("查詢符合部分 title 和(And) description字詞、不分大小寫，按建立時間遞增排序")
        public void testFindByTitleContainsIgnoreCaseAndDescriptionContainsIgnoreCaseOrderByCreatedTimeAsc() {
                // Arrange
                int expectedSize = 4; // 預期結果的大小

                String title = "tes"; // 查詢符合部分 title 字詞，不分大小寫
                String expectedTitle = title.toLowerCase();

                String description = "Des"; // 查詢符合部分 description 字詞，不分大小寫
                String expectedDescription = description.toLowerCase();

                // Act
                List<Note> noteResult = noteRepository
                                .findByTitleContainsIgnoreCaseAndContentContainsIgnoreCaseOrderByCreatedTimeAsc(title,
                                                description);

                // Assert
                assertEquals(expectedSize, noteResult.size(), "查詢結果的大小不正確！");
                assertTrue(noteResult.get(0).getCreatedTime().isBefore(noteResult.get(3).getCreatedTime()),
                                "排序不正確！"); // 檢查排序是否正確
                assertTrue(noteResult.get(0).getTitle().toLowerCase().contains(expectedTitle));
                assertTrue(noteResult.get(0).getContent().toLowerCase().contains(expectedDescription));
        }

        @Test
        @DisplayName("查詢符合部分 title 和(And) description字詞、不分大小寫，按建立時間遞減排序")
        public void testFindByTitleContainsIgnoreCaseAndDescriptionContainsIgnoreCaseOrderByCreatedTimeDesc() {
                // Arrange
                int expectedSize = 4; // 預期結果的大小

                String title = "tes"; // 查詢符合部分 title 字詞，不分大小寫
                String expectedTitle = title.toLowerCase();

                String description = "Des"; // 查詢符合部分 description 字詞，不分大小寫
                String expectedDescription = description.toLowerCase();

                // Act
                List<Note> noteResult = noteRepository
                                .findByTitleContainsIgnoreCaseAndContentContainsIgnoreCaseOrderByCreatedTimeDesc(title,
                                                description);

                // Assert
                assertEquals(expectedSize, noteResult.size(), "查詢結果的大小不正確！");
                assertTrue(noteResult.get(0).getCreatedTime().isAfter(noteResult.get(3).getCreatedTime()),
                                "排序不正確！"); // 檢查排序是否正確
                assertTrue(noteResult.get(0).getTitle().toLowerCase().contains(expectedTitle));
                assertTrue(noteResult.get(0).getContent().toLowerCase().contains(expectedDescription));
        }

        @Test
        @DisplayName("查詢符合部分 description 字詞，按建立時間遞增")
        public void testFindByDescriptionContainingIgnoreCaseOrderByCreatedTimeAsc() {
                // Arrange
                int expectedSize = 6; // 預期結果的大小
                String description = "descr"; // 查詢符合部分 title 字詞，不分大小寫
                String expectedDescription = description.toLowerCase();

                // Act
                List<Note> noteResult = noteRepository
                                .findByContentContainingIgnoreCaseOrderByCreatedTimeAsc(description);

                // Assert
                assertEquals(expectedSize, noteResult.size(), "查詢結果的大小不正確！");
                assertTrue(noteResult.get(0).getCreatedTime().isBefore(noteResult.get(4).getCreatedTime()),
                                "排序不正確！"); // 檢查排序是否正確
                assertTrue(noteResult.get(0).getContent().toLowerCase().contains(expectedDescription));
        }

        @Test
        @DisplayName("查詢符合部分 description 字詞，按建立時間遞減")
        public void testFindByDescriptionContainingIgnoreCaseOrderByCreatedTimeDesc() {
                // Arrange
                int expectedSize = 6; // 預期結果的大小
                String description = "descr"; // 查詢符合部分 title 字詞，不分大小寫
                String expectedDescription = description.toLowerCase();

                // Act
                List<Note> noteResult = noteRepository
                                .findByContentContainingIgnoreCaseOrderByCreatedTimeDesc(description);

                // Assert
                assertEquals(expectedSize, noteResult.size(), "查詢結果的大小不正確！");
                assertTrue(noteResult.get(0).getCreatedTime().isAfter(noteResult.get(4).getCreatedTime()),
                                "排序不正確！"); // 檢查排序是否正確
                assertTrue(noteResult.get(0).getContent().toLowerCase().contains(expectedDescription));
        }

}
