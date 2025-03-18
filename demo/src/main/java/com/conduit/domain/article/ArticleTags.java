package com.conduit.domain.article;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTags {
    private UUID articleId;
    private UUID tagId;
}
