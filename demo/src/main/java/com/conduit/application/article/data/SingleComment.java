package com.conduit.application.article.data;

import com.conduit.application.profile.data.UserProfileResponseDTO;
import com.conduit.domain.article.CommentEntity;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonRootName("comment")
public class SingleComment {
    private UUID id;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String body;
    private UserProfileResponseDTO author;

    public SingleComment(CommentEntity commentEntity, UserProfileResponseDTO author) {
        this.id = commentEntity.getCommentId();
        this.createdAt = commentEntity.getCreatedAt();
        this.updatedAt = commentEntity.getCreatedAt();
        this.body = commentEntity.getCommentBody();
        this.author = author;
    }
}