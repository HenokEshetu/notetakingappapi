package com.notetakingapp.api.note;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    @Column(length = 36, updatable = false)
    private String id;
    @Column(length = 64, nullable = false)
    private String title;
    @Column(length = 1000, nullable = false)
    private String body;
    @Column(length = 36, nullable = false, updatable = false)
    private String userId;

}
