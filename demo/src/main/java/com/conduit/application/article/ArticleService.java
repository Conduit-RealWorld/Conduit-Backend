package com.conduit.application.article;

import com.conduit.application.article.data.MultipleComments;
import com.conduit.application.article.data.SingleArticleDTO;
import com.conduit.application.article.data.SingleComment;
import com.conduit.application.profile.ProfileService;
import com.conduit.application.profile.data.UserProfileResponseDTO;
import com.conduit.common.exception.AlreadyExistsException;
import com.conduit.common.exception.ArticleNotFoundException;
import com.conduit.common.exception.UnauthorizedArticleModificationException;
import com.conduit.common.util.SlugUtil;
import com.conduit.domain.article.ArticleEntity;
import com.conduit.domain.article.CommentEntity;
import com.conduit.domain.article.TagEntity;
import com.conduit.domain.user.UserEntity;
import com.conduit.infrastructure.persistence.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final UserMapper userMapper;
    private final ArticleMapper articleMapper;
    private final TagMapper tagMapper;
    private final FavoriteArticleMapper favoriteArticleMapper;
    private final ArticleTagsMapper articleTagsMapper;
    private final CommentMapper commentMapper;
    private final ProfileService profileService;

    public void createArticleTags(SingleArticleDTO article, UUID articleId) {
        article.getTagList().forEach(
                s -> {
                    if(!tagMapper.exists(s)) {
                        UUID tagId = UUID.randomUUID();
                        tagMapper.create(new TagEntity(tagId, s));
                        articleTagsMapper.create(articleId, tagId);
                    } else {
                        articleTagsMapper.create(tagMapper.getByTagName(s), articleId);
                    }
                }
        );
    }

    public void updateTitle(SingleArticleDTO article, ArticleEntity articleEntity) {
        if(!article.getTitle().isEmpty()) {
            if(articleMapper.exists(article.getTitle()) && !article.getTitle().equals(articleEntity.getTitle())) {
                throw new AlreadyExistsException(article.getTitle());
            }
            articleEntity.setTitle(article.getTitle());
        }
    }

    public void updateDescription(SingleArticleDTO article, ArticleEntity articleEntity) {
        if(!article.getDescription().isEmpty()) {
            articleEntity.setDescription(article.getDescription());
        }
    }

    public void updateContent(SingleArticleDTO article, ArticleEntity articleEntity) {
        if(!article.getBody().isEmpty()) {
            articleEntity.setContent(article.getBody());
        }
    }

    public SingleArticleDTO createArticle(SingleArticleDTO article, String authorName) {
        if(articleMapper.exists(article.getTitle())) {
            throw new AlreadyExistsException(article.getTitle());
        }

        UserEntity authorEntity = userMapper.findByUsername(authorName);
        UUID articleId = UUID.randomUUID();
        ArticleEntity articleEntity = new ArticleEntity(article, articleId, authorEntity.getId());
        articleMapper.createArticle(articleEntity);

        UserProfileResponseDTO authorProfile = profileService.getProfile(authorName, authorName);
        article.init(articleMapper.getArticleById(articleId), authorProfile);
        createArticleTags(article, articleId);

        return article;
    }

    public SingleArticleDTO getArticle(String slug, String username) {
        String title = SlugUtil.toSlug(slug);
        if(!articleMapper.exists(title)) {
            throw new ArticleNotFoundException(title);
        }
        ArticleEntity articleEntity = articleMapper.getArticleByTitle(title);
        String authorName = userMapper.findByUserId(articleEntity.getAuthorId()).getUsername();
        UserProfileResponseDTO authorProfile = profileService.getProfile(authorName, username);
        return new SingleArticleDTO(articleEntity, authorProfile,
                favoriteArticleMapper.ifFavorited(articleEntity.getAuthorId(), articleEntity.getArticleId()),
                favoriteArticleMapper.favoriteCount(articleEntity.getArticleId()));
    }

    public SingleArticleDTO updateArticle(String slug, SingleArticleDTO article, String authorName) {
        String oldTitle = SlugUtil.fromSlug(slug);
        UUID userId = userMapper.findByUsername(authorName).getId();
        if(!articleMapper.exists(slug)) {
            throw new ArticleNotFoundException(oldTitle);
        }
        ArticleEntity articleEntity = articleMapper.getArticleByTitle(oldTitle);
        if(!userId.equals(articleEntity.getAuthorId())) {
            throw new UnauthorizedArticleModificationException(oldTitle);
        }

        updateTitle(article, articleEntity);
        updateDescription(article, articleEntity);
        updateContent(article, articleEntity);
        articleMapper.updateArticle(articleEntity);

        UserProfileResponseDTO authorProfile = profileService.getProfile(authorName, authorName);
        return new SingleArticleDTO(articleEntity, authorProfile,
                favoriteArticleMapper.ifFavorited(articleEntity.getAuthorId(), articleEntity.getArticleId()),
                favoriteArticleMapper.favoriteCount(articleEntity.getArticleId()));
    }

    public SingleArticleDTO favoriteArticle(String slug, String username) {
        String title = SlugUtil.fromSlug(slug);
        if(!articleMapper.exists(title)) {
            throw new ArticleNotFoundException(slug);
        }
        ArticleEntity articleEntity = articleMapper.getArticleByTitle(slug);
        UserEntity userEntity = userMapper.findByUsername(username);
        if(!favoriteArticleMapper.ifFavorited(userEntity.getId(), articleEntity.getArticleId())) {
            favoriteArticleMapper.favorite(userEntity.getId(), articleEntity.getArticleId());
        }
        UserProfileResponseDTO authorProfile = profileService.getProfile(userMapper.findByUserId(articleEntity.getAuthorId()).getUsername(), username);
        return new SingleArticleDTO(articleEntity, authorProfile,
                favoriteArticleMapper.ifFavorited(articleEntity.getAuthorId(), articleEntity.getArticleId()),
                favoriteArticleMapper.favoriteCount(articleEntity.getArticleId()));
    }

    public SingleArticleDTO unfavoriteArticle(String slug, String username) {
        String title = SlugUtil.fromSlug(slug);
        if(!articleMapper.exists(title)) {
            throw new ArticleNotFoundException(slug);
        }
        ArticleEntity articleEntity = articleMapper.getArticleByTitle(slug);
        UserEntity userEntity = userMapper.findByUsername(username);
        favoriteArticleMapper.unfavorite(userEntity.getId(), articleEntity.getArticleId());
        UserProfileResponseDTO authorProfile = profileService.getProfile(userMapper.findByUserId(articleEntity.getAuthorId()).getUsername(), username);
        return new SingleArticleDTO(articleEntity, authorProfile,
                favoriteArticleMapper.ifFavorited(articleEntity.getAuthorId(), articleEntity.getArticleId()),
                favoriteArticleMapper.favoriteCount(articleEntity.getArticleId()));
    }

    public SingleComment addComment(String slug, SingleComment singleComment, String username) {
        String title = SlugUtil.fromSlug(slug);
        if(!articleMapper.exists(title)) {
            throw new ArticleNotFoundException(title);
        }
        ArticleEntity articleEntity = articleMapper.getArticleByTitle(title);
        UserEntity userEntity = userMapper.findByUsername(username);
        UserProfileResponseDTO authorProfile = profileService.getProfile(username, username);
        CommentEntity commentEntity = new CommentEntity(articleEntity.getArticleId(), userEntity.getId(), authorProfile.getUsername());
        commentMapper.create(commentEntity);
        return new SingleComment(commentEntity, authorProfile);
    }

    public void deleteComment(String slug, UUID commentId, String username) {
        String title = SlugUtil.fromSlug(slug);
        if(!articleMapper.exists(title)) {
            throw new ArticleNotFoundException(title);
        }
        ArticleEntity articleEntity = articleMapper.getArticleByTitle(title);
        CommentEntity commentEntity = commentMapper.getByCommentId(commentId);
        if(!articleEntity.getArticleId().equals(commentEntity.getArticleId()) ||
                !commentEntity.getUserId().equals(userMapper.findByUsername(username).getId())) {
            throw new UnauthorizedArticleModificationException(commentId.toString());
        }
        commentMapper.deleteByCommentId(commentId);
    }

    public MultipleComments getComments(String slug, String username) {
        String title = SlugUtil.fromSlug(slug);
        if(!articleMapper.exists(title)) {
            throw new ArticleNotFoundException(title);
        }
        ArticleEntity articleEntity = articleMapper.getArticleByTitle(title);
        List<CommentEntity> allComments = commentMapper.getAllCommentsByArticleId(articleEntity.getArticleId());
        MultipleComments multipleComments = new MultipleComments();
        allComments.forEach(
                comment -> {
                    String authorName = userMapper.findByUserId(comment.getUserId()).getUsername();
                    UserProfileResponseDTO authorProfile = profileService.getProfile(authorName, username);
                    multipleComments.addComment(new SingleComment(comment, authorProfile));
                }
        );
        return multipleComments;
    }
}
