package com.notetakingapp.api.user;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserRegisterForm {

    private String id;
    private String fullName;
    private String email;
    private String password;
    private String profileImageUrl;

}
