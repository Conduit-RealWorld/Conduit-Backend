package com.conduit.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.UUID;

@Mapper
public interface ArticleTagsMapper {
    @Insert("INSERT INTO article_tags (article_id, tag_id) VALUES (#{articleId}, #{tagId})")
    void create(@Param("articleId") UUID articleId, @Param("tagId") UUID tagId);

    // Get all tag IDs associated with an article
    @Select("SELECT tag_id FROM article_tags WHERE article_id = #{articleId}")
    List<UUID> getAllTagsByArticle(@Param("articleId") UUID articleId);

    // Get all article IDs associated with a tag
    @Select("SELECT article_id FROM article_tags WHERE tag_id = #{tagId}")
    List<UUID> getAllArticlesByTag(@Param("tagId") UUID tagId);

    @Delete("DELETE FROM article_tags WHERE article_id = #{articleId}")
    void deleteByArticleId(@Param("articleId") UUID articleId);
}
