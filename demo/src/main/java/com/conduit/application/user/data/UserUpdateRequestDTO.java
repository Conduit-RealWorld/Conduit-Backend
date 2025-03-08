package com.conduit.application.user.data;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("user")
public class UserUpdateRequestDTO {
    @Email(message = "Email should be valid")
    private String email;
    private String bio;
    private String image;
}
