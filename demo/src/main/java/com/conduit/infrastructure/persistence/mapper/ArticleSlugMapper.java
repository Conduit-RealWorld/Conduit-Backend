package com.conduit.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.*;
import java.util.UUID;

@Mapper
public interface ArticleSlugMapper {
    @Insert("INSERT INTO article_slugs (article_id, article_slug) VALUES (#{articleId}, #{articleSlug})")
    void create(UUID articleId, String articleSlug);

    @Select("SELECT article_id FROM  article_slugs WHERE article_slug = #{articleSlug}")
    UUID getArticleIdByArticleSlug(String articleSlug);

    @Update("UPDATE article_slugs SET article_slug = #{articleSlug} WHERE article_id = #{articleId}")
    void updateSlug(UUID articleId, String articleSlug);

    @Delete("DELETE FROM article_slugs WHERE article_id = #{articleId}")
    void deleteByArticleId(UUID articleId);
}