package com.conduit.domain.article;

import com.conduit.application.article.data.SingleArticleDTO;
import lombok.*;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ArticleEntity {
    private UUID articleId;
    private UUID authorId;
    private String title;
    private String description;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private int favoriteCount;

    public ArticleEntity(SingleArticleDTO article, UUID articleId, UUID authorId) {
        this.setTitle(article.getTitle());
        this.setDescription(article.getDescription());
        this.setArticleId(articleId);
        this.setAuthorId(authorId);
        this.setContent(article.getBody());
        this.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        this.setUpdatedAt(this.createdAt);
    }
}
