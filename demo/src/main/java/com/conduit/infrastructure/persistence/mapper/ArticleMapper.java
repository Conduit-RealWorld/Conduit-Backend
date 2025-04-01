package com.conduit.infrastructure.persistence.mapper;

import com.conduit.domain.article.ArticleEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
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

    @Select(
            "<script> "+
            "SELECT DISTINCT a.* "+
            "FROM article a "+
            "LEFT JOIN article_tags at ON a.article_id = at.article_id "+
            "LEFT JOIN tag t ON t.tag_id = at.tag_id "+
            "<where> "+
            "  <if test='tag != null'> "+
            "    AND t.name = #{tag} "+
            "  </if> "+
            "  <if test='author != null'> "+
            "    AND a.author_id = (SELECT id FROM users WHERE username = #{author}) "+
            "  </if> "+
            "  <if test='favorited != null'> "+
            "    AND a.article_id IN ( "+
            "      SELECT article_id FROM favorite_article "+
            "      WHERE user_id = (SELECT id FROM users WHERE username = #{favorited}) "+
            "    ) "+
            "  </if> "+
            "</where> "+
            "ORDER BY a.created_at DESC "+
            "LIMIT #{limit} OFFSET #{offset} "+
            "</script>"
    )
    List<ArticleEntity> findArticles(
            @Param("tag") String tag,
            @Param("author") String author,
            @Param("favorited") String favorited,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}