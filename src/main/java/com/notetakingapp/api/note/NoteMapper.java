package com.notetakingapp.api.note;

import java.util.ArrayList;
import java.util.List;

public class NoteMapper {

    public static Note getNote(NoteDto noteDto) {
        return Note.builder()
                .noteId(noteDto.getNoteId())
                .title(noteDto.getTitle())
                .body(noteDto.getBody())
                .userId(noteDto.getUserId())
                .build();
    }

    public static NoteDto getNoteDto(Note note) {
        return NoteDto.builder()
                .noteId(note.getNoteId())
                .title(note.getTitle())
                .body(note.getBody())
                .userId(note.getUserId())
                .build();
    }

    public static List<Note> getNotes(List<NoteDto> noteDtoList) {
        List<Note> notes = new ArrayList<>();
        for (NoteDto noteDto : noteDtoList)
            notes.add(getNote(noteDto));
        return notes;
    }

    public static List<NoteDto> getNoteDtos(List<Note> notes) {
        List<NoteDto> noteDtoList = new ArrayList<>();
        for (Note note : notes)
            noteDtoList.add(getNoteDto(note));
        return noteDtoList;
    }

}
