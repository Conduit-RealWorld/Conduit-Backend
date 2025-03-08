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
@JsonRootName("profile")
public class UserProfileResponseDTO {
    private String username;
    private String bio;
    private String image;
    private boolean following;
    public UserProfileResponseDTO(UserEntity user, boolean following) {
        this.username = user.getUsername();
        this.bio = user.getBio();
        this.image = user.getImage();
        this.following = following;
    }
}
