package com.notetakingapp.api.note;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NoteDto {

    private String noteId;
    private String title;
    private String body;
    private String userId;

}
