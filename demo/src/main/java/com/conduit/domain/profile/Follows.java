package com.conduit.domain.profile;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Follows {
    private UUID id;
    private UUID followedID;
    private UUID followeeID;
}
