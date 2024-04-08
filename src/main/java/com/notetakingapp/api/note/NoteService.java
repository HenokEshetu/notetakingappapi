package com.notetakingapp.api.note;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNote(String id) {
        return noteRepository.findById(id);
    }

    public Note createNote(Note note) {
        return noteRepository.saveAndFlush(note);
    }

    public Note updateNote(Note note) {
        return noteRepository.saveAndFlush(note);
    }

    public String deleteNote(String id) {
        noteRepository.deleteById(id);
        return "OK";
    }

}
