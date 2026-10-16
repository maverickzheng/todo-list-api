package com.example.note.feature.note;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.note.common.GenericAPIResponse;
import com.example.note.feature.note.NoteDTO;
import com.example.note.feature.note.Note;
import com.example.note.feature.note.NoteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

// @PreAuthorize("hasRole('ADMIN')") // 控制訪問權限
@Tag(name = "Note API", description = "待辦事項 API 說明")
@RestController
@RequestMapping("/api/notes")
public class NoteController {

        // @Autowired
        // NoteService noteService;

        private final NoteService noteService;

        public NoteController(NoteService noteService) {
                this.noteService = noteService;
        }

        // 深度挖掘Java列舉(enum)
        // https://www.tpisoftware.com/tpu/articleDetails/1432
        // 以枚舉建立排序的選項
        public enum Orders {
                Title("title"),
                Content("content"),
                CreatedTime("createdTime"),
                UpdatedTime("updatedTime");

                private String label;

                Orders(String label) {
                        this.label = label;
                }

                public String getLabel() {
                        return label;
                }
        }

        final String noteNotFoundExample = """
                        {
                                "success": false,
                                "statusCode": 404,
                                "time": "2000-01-01 00:00:00 TST",
                                "message": "No note by ID: 0",
                                "data": null
                        }
                        """;

        @GetMapping
        @Operation(summary = "顯示所有待辦事項", description = "", responses = {
                        @ApiResponse(responseCode = "200", description = "Successfully obtained all notes!", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Note not found.", content = @Content)
        })
        public ResponseEntity<GenericAPIResponse<Page<Note>>> getAllNotes(
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "5") int size,
                        @RequestParam(value = "direction", defaultValue = "ASC") Direction direction,
                        @RequestParam(value = "order", defaultValue = "CreatedTime") Orders order) {

                Page<Note> notes = noteService.getAllNotes(PageRequest.of(page, size, Sort.by(direction, order.label)));

                return ResponseEntity.ok(GenericAPIResponse.success(200, "成功取得所有待辦事項。", notes));
        }

        @GetMapping("/{id}")
        @Operation(summary = "顯示待辦事項", description = "透過ID查詢待辦事項", responses = {
                        @ApiResponse(responseCode = "200", description = "Successfully obtained note!", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Note not found.", content = @Content(examples = @ExampleObject(value = noteNotFoundExample)))
        })
        public ResponseEntity<GenericAPIResponse<Note>> getNoteById(
                        @Parameter(description = "ID of note.") @PathVariable("id") Long id) {

                Note note = noteService.getNoteById(id);

                return ResponseEntity.ok(GenericAPIResponse.success(200, "成功取得待辦事項。",
                                note));

        }

        @PostMapping
        @Operation(summary = "新增待辦事項", description = "", responses = {
                        @ApiResponse(responseCode = "201", description = "Note created successfully!", content = @Content(examples = @ExampleObject(value = """
                                        {
                                                "success": true,
                                                "statusCode": 201,
                                                "time": "2000-01-01 00:00:00 TST",
                                                "message": "成功建立待辦事項",
                                                "data":  {
                                                        "title": "Title",
                                                        "description": "Description",
                                                        "status": false
                                                        }
                                        }
                                        """)))
        })
        public ResponseEntity<GenericAPIResponse<Note>> createNote(
                        @RequestBody(required = true) @Valid NoteDTO noteDTO) {

                Note newnote = noteService.createNote(noteDTO);

                // 建立標頭中的Location
                URI location = ServletUriComponentsBuilder
                                .fromCurrentRequestUri()
                                .path("/{id}").buildAndExpand(newnote.getNoteId())
                                .toUri();

                return ResponseEntity.created(location).body(GenericAPIResponse.success(201, "成功建立待辦事項。", newnote));
        }

        @PutMapping("/{id}")
        @Operation(summary = "更新待辦事項", description = "", responses = {
                        @ApiResponse(responseCode = "204", description = "Note update successfully!", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Note not found.", content = @Content)
        })
        public ResponseEntity<GenericAPIResponse<Void>> updateNote(
                        @Parameter(description = "ID of note.") @PathVariable("id") Long id,
                        @RequestBody @Valid NoteDTO renewNoteDTO) {

                noteService.updateNote(id, renewNoteDTO);

                return ResponseEntity.status(HttpStatus.OK)
                                .body(GenericAPIResponse.success(204, "成功更新待辦事項。", null));

        }

        @DeleteMapping("/{id}")
        @Operation(summary = "刪除待辦事項", description = "", responses = {
                        @ApiResponse(responseCode = "204", description = "Note delete successfully!", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Note not found.", content = @Content)
        })
        public ResponseEntity<GenericAPIResponse<Void>> deleteNote(
                        @Parameter(description = "ID of note.") @PathVariable("id") Long id) {
                noteService.deleteNote(id);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(GenericAPIResponse.success(204, "成功刪除待辦事項。", null));

        }
        /*
         * @GetMapping("/search/notcompleted")
         * 
         * @Operation(summary = "列出未完成待辦事項", description = "列出未完成待辦事項，並按建立時間遞增或遞減排序",
         * responses = {
         * 
         * @ApiResponse(responseCode = "200", description = "Successfully obtained
         * note!", content = @Content),
         * 
         * @ApiResponse(responseCode = "404", description = "Note not found.", content =
         * 
         * @Content)
         * })
         * public ResponseEntity<GenericAPIResponse<Page<Note>>> searchNotCompleted(
         * 
         * @RequestParam(value = "increment", required = true, defaultValue = "true")
         * boolean order) {
         * Page<Note> notes = noteService.notCompleted(order);
         * return ResponseEntity.status(HttpStatus.OK)
         * .body(GenericAPIResponse.success(200, "成功取得待辦事項。", notes));
         * 
         * }
         */

        @GetMapping("/search/notcompleted")
        @Operation(summary = "未完成待辦事項", description = "列出未完成待辦事項", responses = {
                        @ApiResponse(responseCode = "200", description = "Successfully obtained note!", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Note not found.", content = @Content)
        })
        public ResponseEntity<GenericAPIResponse<Page<Note>>> searchNotCompleted(
                        @RequestParam(value = "increment", required = true, defaultValue = "true") boolean increment,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "order", defaultValue = "CreatedTime") Orders order) {
                Page<Note> notes = noteService.searchNotCompleted(increment, page, order.label);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(GenericAPIResponse.success(200, "成功取得待辦事項。", notes));

        }

        @GetMapping("/search/title")
        @Operation(summary = "以標題尋找待辦事項", description = "透過標題尋找待辦事項", responses = {
                        @ApiResponse(responseCode = "200", description = "Successfully obtained note!", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Note not found.", content = @Content)
        })
        public ResponseEntity<GenericAPIResponse<Page<Note>>> searchTitle(
                        @RequestParam(value = "title", required = true, defaultValue = "") String title,
                        @RequestParam(value = "increment", required = true, defaultValue = "true") boolean increment,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "order", defaultValue = "CreatedTime") Orders order) {
                // List<Note> notes = noteService.searchNotesByTitle(title, order);

                Page<Note> notes = noteService.searchNotesByTitle(title, increment, page, order.label);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(GenericAPIResponse.success(200, "成功取得待辦事項。", notes));

        }

        @GetMapping("/search/exact")
        @Operation(summary = "精確搜尋待辦事項", description = "透過標題和內容尋找待辦事項，並按建立時間遞增或是遞減排序", responses = {
                        @ApiResponse(responseCode = "200", description = "Successfully obtained note!", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Note not found.", content = @Content)
        })
        public ResponseEntity<GenericAPIResponse<List<Note>>> searchExact(
                        @RequestParam(value = "title", required = true, defaultValue = "") @NotBlank(message = "標題搜尋不得為空") String title,
                        @RequestParam(value = "content", required = true, defaultValue = "") @NotBlank(message = "內容搜尋不得為空") String content,
                        @RequestParam(value = "increment", required = true, defaultValue = "true") boolean increment) {

                List<Note> notes = noteService.searchExact(title, content, increment);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(GenericAPIResponse.success(200, "成功取得待辦事項。", notes));

        }

}
