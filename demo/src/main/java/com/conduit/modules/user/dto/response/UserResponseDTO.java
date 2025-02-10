package com.conduit.modules.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {
    private String username;
    private String email;
    private String token;
    private String bio;
    private String image;
}
