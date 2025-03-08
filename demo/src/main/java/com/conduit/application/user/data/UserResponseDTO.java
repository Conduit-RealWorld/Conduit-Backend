package com.conduit.application.user.data;

import com.conduit.domain.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("user")
public class UserResponseDTO {
    private String username;
    private String email;
    private String token;
    private String bio;
    private String image;

    public UserResponseDTO(UserEntity user, String token) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.token = token;
        this.bio = user.getBio();
        this.image = user.getImage();
    }
}
