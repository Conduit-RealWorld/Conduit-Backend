package com.conduit.domain.article;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleSlug {
    private UUID articleId;
    private String articleSlug;
}