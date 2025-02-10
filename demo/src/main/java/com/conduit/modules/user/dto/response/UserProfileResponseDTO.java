package com.conduit.modules.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserProfileResponseDTO {
    private String username;
    private String bio;
    private String image;
    private boolean following;
}
