package com.conduit.domain.user;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity {
    private UUID id;
    private String username;
    private String email;
    private String password;
    private String bio;
    private String image;
}
