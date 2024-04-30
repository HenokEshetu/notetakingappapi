package com.notetakingapp.api.note;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/note")
@AllArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<List<Note>> getNotes() throws Exception {
        return new ResponseEntity<>(noteService.getNotes(), HttpStatus.OK);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<Note> getNote(@PathVariable String noteId) throws Exception {
        return new ResponseEntity<>(noteService.getNote(noteId), HttpStatus.OK);
    }

    @GetMapping("/user-id/{userId}")
    public ResponseEntity<List<Note>> getNotesByUserId(@PathVariable String userId) throws Exception {
        return new ResponseEntity<>(noteService.getNotesById(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody NoteDto note) throws Exception {
        NoteDto encryptedNote = noteService.encryptNote(note);
        return new ResponseEntity<>(noteService.createNote(encryptedNote), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Note> updateNote(@RequestBody NoteDto note) throws Exception {
        NoteDto encryptedNote = noteService.encryptNote(note);
        return new ResponseEntity<>(noteService.updateNote(encryptedNote), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteNote(@PathVariable String id) {
        return new ResponseEntity<>(noteService.deleteNote(id), HttpStatus.OK);
    }

}
