package com.notetakingapp.api.note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, String> {
    @Query("SELECT n FROM Note n WHERE n.userId = ?1")
    List<Note> findNotesByUserId(String userId);
    @Query("SELECT n FROM Note n WHERE n.noteId = ?1")
    Optional<Note> findByNoteId(String noteId);
}
