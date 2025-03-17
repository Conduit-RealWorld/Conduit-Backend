package com.conduit.infrastructure.persistence.mapper;

import com.conduit.domain.article.CommentEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CommentMapper {
    @Insert("INSERT INTO comment (comment_id, parent_comment_id, article_id, user_id, comment_body, created_at) " +
            "VALUES (#{comment.commentId}, #{comment.parentId}, #{comment.articleId}, #{comment.userId}, #{comment.comment}, #{comment.createdAt})")
    void create(@Param("comment") CommentEntity comment);

    @Delete("DELETE FROM comment WHERE comment_id = #{commentId}")
    void deleteByCommentId(@Param("commentId") UUID commentId);

    @Select("SELECT * FROM comment WHERE comment_id = #{commentId}")
    CommentEntity getByCommentId(@Param("commentId") UUID commentId);

    @Select("Select * From comment WHERE article_id = #{articleId}")
    List<CommentEntity> getAllCommentsByArticleId(@Param("articleId") UUID articleId);
}
