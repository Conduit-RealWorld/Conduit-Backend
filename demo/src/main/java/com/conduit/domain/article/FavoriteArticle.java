package com.conduit.domain.article;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteArticle {
    UUID userId;
    UUID articleId;
}
