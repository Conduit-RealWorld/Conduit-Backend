package com.conduit.modules.user.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class UserDTO {
    private Optional<String> username = Optional.empty();
    private String email;
    private Optional<String> password = Optional.empty();
    private Optional<String> bio = Optional.empty();
    private Optional<String> image = Optional.empty();
}
