package com.notetakingapp.api.note;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/note")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public ResponseEntity<List<Note>> getNotes() {
        return new ResponseEntity<>(noteService.getNotes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Note>> getNote(@PathVariable String id) {
        return new ResponseEntity<>(noteService.getNote(id), HttpStatus.OK);
    }

    @GetMapping("/by-user-id/{userId}")
    public ResponseEntity<List<Note>> getNoteByUserId(@PathVariable String userId) {
        return new ResponseEntity<>(noteService.getNotesById(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        return new ResponseEntity<>(noteService.createNote(note), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Note> updateNote(@RequestBody Note note) {
        return new ResponseEntity<>(noteService.updateNote(note), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteNote(@PathVariable String id) {
        return new ResponseEntity<>(noteService.deleteNote(id), HttpStatus.OK);
    }

}
