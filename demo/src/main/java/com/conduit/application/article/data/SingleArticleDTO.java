package com.conduit.application.article.data;

import com.conduit.application.profile.data.UserProfileResponseDTO;
import com.conduit.common.util.SlugUtil;
import com.conduit.domain.article.ArticleEntity;
import com.conduit.domain.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName("article")
public class SingleArticleDTO {
    private String slug;
    private String title;
    private String description;
    private String body;
    private ArrayList<String> tagList;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean favorited;
    private int favoritesCount;
    private UserProfileResponseDTO author;

    public SingleArticleDTO(ArticleEntity articleEntity, UserProfileResponseDTO author, boolean favorited, int favoritesCount) {
        String title = articleEntity.getTitle();
        String slug = SlugUtil.toSlug(title);
        this.setTitle(title);
        this.setSlug(slug);
        this.setDescription(articleEntity.getDescription());
        this.setCreatedAt(articleEntity.getCreatedAt());
        this.setUpdatedAt(articleEntity.getUpdatedAt());
        this.setFavorited(favorited);
        this.setFavoritesCount(favoritesCount);
        this.setAuthor(author);
    }

    public void init(ArticleEntity article, UserProfileResponseDTO author) {
        this.setSlug(SlugUtil.toSlug(article.getTitle()));
        this.setFavorited(false);
        this.setFavoritesCount(0);
        this.setAuthor(author);
        this.setCreatedAt(article.getCreatedAt());
        this.setUpdatedAt(article.getUpdatedAt());
    }
}
