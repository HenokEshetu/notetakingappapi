package com.notetakingapp.api.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getNotes() {
        return noteRepository.findAll();
    }

    public Optional<Note> getNote(String id) {
        return noteRepository.findById(id);
    }

    public List<Note> getNotesById(String userId) {
        return noteRepository.findNotesByUserId(userId);
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
