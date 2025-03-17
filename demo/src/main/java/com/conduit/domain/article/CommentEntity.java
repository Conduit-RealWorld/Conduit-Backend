package com.conduit.domain.article;

import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentEntity {
    private UUID parentId;
    private UUID commentId;
    private UUID articleId;
    private UUID userId;
    private String comment;
    private Timestamp created_at;

    public CommentEntity(UUID articleId, UUID userId, String comment) {
        this.commentId = UUID.randomUUID();
        this.articleId = articleId;
        this.userId = userId;
        this.comment = comment;
        this.created_at = new Timestamp(System.currentTimeMillis());
    }
}
