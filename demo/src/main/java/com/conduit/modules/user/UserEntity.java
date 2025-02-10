package com.conduit.modules.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserEntity {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String bio;
    private String image;
}
