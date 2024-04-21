package com.notetakingapp.api.note;

import com.notetakingapp.api.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    @Id
    @Column(length = 36, updatable = false, name = "note_id")
    private String noteId;
    @Column(length = 64, nullable = false)
    private String title;
    @Column(length = 1000, nullable = false)
    private String body;
    @Column(length = 36, updatable = false, name = "user_id")
    private String userId;

}
