package com.conduit.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.*;

import java.util.UUID;

@Mapper
public interface FavoriteArticleMapper {
    @Select("SELECT COUNT(*) FROM favorite_article WHERE article_id = #{articleId}")
    int favoriteCount(@Param("articleId") UUID articleId);

    @Select("SELECT EXISTS(SELECT 1 FROM favorite_article WHERE user_id = #{userId} AND article_id = #{articleId})")
    boolean ifFavorited(@Param("userId") UUID userId, @Param("articleId") UUID articleId);

    @Insert("INSERT INTO favorite_article (user_id, article_id) VALUES (#{userId}, #{articleId})")
    void favorite(@Param("userId") UUID userId, @Param("articleId") UUID articleId);

    @Delete("DELETE FROM favorite_article WHERE user_id = #{userId} AND article_id = #{articleId}")
    void unfavorite(@Param("userId") UUID userId, @Param("articleId") UUID articleId);

    @Delete("DELETE FROM favorite_article WHERE article_id = #{articleId}")
    void deleteByArticleId(@Param("articleId") UUID articleId);
}
