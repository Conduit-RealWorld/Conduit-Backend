package com.conduit.infrastructure.persistence.mapper;

import com.conduit.domain.article.ArticleEntity;
import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface ArticleMapper {
    @Insert("INSERT INTO article (article_id, author_id, title, description, content, created_at, updated_at) " +
            "VALUES (#{articleId}, #{authorId}, #{title}, #{description}, #{content}, #{createdAt}, #{createdAt})")
    void createArticle(ArticleEntity article);

    @Select("SELECT * FROM article WHERE article_id = #{articleId, jdbcType=OTHER}")
    ArticleEntity getArticleById(UUID articleId);

    @Select("SELECT EXISTS(SELECT 1 FROM article WHERE title = #{title})")
    boolean exists(String title);

    @Select("SELECT * FROM article WHERE title = #{title}")
    ArticleEntity getArticleByTitle(String title);

    @Update("UPDATE article SET title = #{title}, description = #{description}, content = #{content}, updated_at = NOW() " +
            "WHERE article_id = #{articleId}")
    void updateArticle(ArticleEntity article);

    @Delete("DELETE FROM article WHERE article_id = #{articleId}")
    void deleteArticleById(UUID articleId);
}