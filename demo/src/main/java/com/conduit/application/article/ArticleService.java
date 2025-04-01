package com.conduit.application.article;

import com.conduit.application.article.data.MultipleArticlesDTO;
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
import org.springframework.transaction.annotation.Transactional;

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
    private final ArticleSlugMapper articleSlugMapper;
    private final ProfileService profileService;

    private void createArticleTags(SingleArticleDTO article, UUID articleId) {
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

    private void updateArticleFields(SingleArticleDTO article, ArticleEntity articleEntity) {
        if (article.getTitle() != null && !article.getTitle().isBlank()) {
            if (articleMapper.exists(article.getTitle()) && !article.getTitle().equals(articleEntity.getTitle())) {
                throw new AlreadyExistsException(article.getTitle());
            }
            articleEntity.setTitle(article.getTitle());
            articleSlugMapper.updateSlug(articleEntity.getArticleId(), SlugUtil.toSlug(article.getTitle()));
        }

        if (article.getDescription() != null && !article.getDescription().isBlank()) {
            articleEntity.setDescription(article.getDescription());
        }

        if (article.getBody() != null && !article.getBody().isBlank()) {
            articleEntity.setContent(article.getBody());
        }
    }

    private ArticleEntity getArticleEntityBySlug(String slug) {
        UUID articleId = articleSlugMapper.getArticleIdByArticleSlug(slug);
        if(articleId == null) {
            throw new ArticleNotFoundException(SlugUtil.fromSlug(slug));
        }
        return articleMapper.getArticleById(articleId);
    }

    private SingleArticleDTO buildSingleArticleDTO(ArticleEntity articleEntity, UserProfileResponseDTO authorProfile, UUID currentUserId, boolean withBody) {
        return new SingleArticleDTO(
                articleEntity,
                authorProfile,
                favoriteArticleMapper.ifFavorited(currentUserId, articleEntity.getArticleId()),
                favoriteArticleMapper.favoriteCount(articleEntity.getArticleId()),
                withBody
        );
    }

    @Transactional
    public SingleArticleDTO createArticle(SingleArticleDTO article, String authorName) {
        if(articleMapper.exists(article.getTitle())) {
            throw new AlreadyExistsException(article.getTitle());
        }

        UserEntity authorEntity = userMapper.findByUsername(authorName);
        UUID articleId = UUID.randomUUID();
        ArticleEntity articleEntity = new ArticleEntity(article, articleId, authorEntity.getId());
        articleMapper.createArticle(articleEntity);
        articleSlugMapper.create(articleId, SlugUtil.toSlug(article.getTitle()));

        UserProfileResponseDTO authorProfile = profileService.getProfile(authorName, authorName);
        article.init(articleMapper.getArticleById(articleId), authorProfile);
        createArticleTags(article, articleId);
        return article;
    }

    @Transactional
    public SingleArticleDTO getArticle(String slug, String username) {
        ArticleEntity articleEntity = getArticleEntityBySlug(slug);
        String authorName = userMapper.findByUserId(articleEntity.getAuthorId()).getUsername();
        UserProfileResponseDTO authorProfile = profileService.getProfile(authorName, username);

        return buildSingleArticleDTO(articleEntity, authorProfile, userMapper.findByUsername(username).getId(), true);
    }

    @Transactional
    public SingleArticleDTO updateArticle(String slug, SingleArticleDTO article, String authorName) {
        ArticleEntity articleEntity = getArticleEntityBySlug(slug);
        UUID userId = userMapper.findByUsername(authorName).getId();
        if(!userId.equals(articleEntity.getAuthorId())) {
            throw new UnauthorizedArticleModificationException(SlugUtil.fromSlug(slug));
        }

        updateArticleFields(article, articleEntity);
        articleMapper.updateArticle(articleEntity);
        UserProfileResponseDTO authorProfile = profileService.getProfile(authorName, authorName);
        return buildSingleArticleDTO(articleEntity, authorProfile, userId, true);
    }

    @Transactional
    public SingleArticleDTO favoriteArticle(String slug, String username) {
        ArticleEntity articleEntity = getArticleEntityBySlug(slug);
        UserEntity userEntity = userMapper.findByUsername(username);
        if(!favoriteArticleMapper.ifFavorited(userEntity.getId(), articleEntity.getArticleId())) {
            favoriteArticleMapper.favorite(userEntity.getId(), articleEntity.getArticleId());
        }
        UserProfileResponseDTO authorProfile = profileService.getProfile(userMapper.findByUserId(articleEntity.getAuthorId()).getUsername(), username);
        return buildSingleArticleDTO(articleEntity, authorProfile, userEntity.getId(), true);
    }

    @Transactional
    public SingleArticleDTO unfavoriteArticle(String slug, String username) {
        ArticleEntity articleEntity = getArticleEntityBySlug(slug);
        UserEntity userEntity = userMapper.findByUsername(username);
        favoriteArticleMapper.unfavorite(userEntity.getId(), articleEntity.getArticleId());
        UserProfileResponseDTO authorProfile = profileService.getProfile(userMapper.findByUserId(articleEntity.getAuthorId()).getUsername(), username);
        return buildSingleArticleDTO(articleEntity, authorProfile, userEntity.getId(), true);
    }

    @Transactional
    public void deleteArticle(String slug, String username) {
        ArticleEntity articleEntity = getArticleEntityBySlug(slug);
        UserEntity userEntity = userMapper.findByUsername(username);
        if(!userEntity.getId().equals(articleEntity.getAuthorId())) {
            throw new UnauthorizedArticleModificationException(SlugUtil.fromSlug(slug));
        }
        articleTagsMapper.deleteByArticleId(articleEntity.getArticleId());
        favoriteArticleMapper.deleteByArticleId(articleEntity.getArticleId());
        commentMapper.deleteByArticleId(articleEntity.getArticleId());
        articleSlugMapper.deleteByArticleId(articleEntity.getArticleId());
        articleMapper.deleteArticleById(articleEntity.getArticleId());
    }

    @Transactional
    public MultipleArticlesDTO listArticles(String tag, String author, String favoriteBy, int limit, int offset, String username) {
        List<ArticleEntity> articles = articleMapper.findArticles(tag, author, favoriteBy, limit, offset);
        UUID currentUserId = userMapper.findByUsername(username).getId();

        MultipleArticlesDTO multipleArticlesDTO = new MultipleArticlesDTO();
        articles.forEach(
                articleEntity -> {
                    UserProfileResponseDTO authorProfile = profileService.getProfile(userMapper.findByUserId(articleEntity.getAuthorId()).getUsername(), username);
                    multipleArticlesDTO.getArticles().addLast(buildSingleArticleDTO(articleEntity, authorProfile, currentUserId, false));
                }
        );
        multipleArticlesDTO.setArticlesCount(articles.size());
        return multipleArticlesDTO;
    }

    @Transactional
    public SingleComment addComment(String slug, SingleComment singleComment, String username) {
        ArticleEntity articleEntity = getArticleEntityBySlug(slug);
        UserEntity userEntity = userMapper.findByUsername(username);
        UserProfileResponseDTO authorProfile = profileService.getProfile(username, username);
        CommentEntity commentEntity = new CommentEntity(articleEntity.getArticleId(), userEntity.getId(), singleComment.getBody());
        commentMapper.create(commentEntity);
        return new SingleComment(commentEntity, authorProfile);
    }

    @Transactional
    public void deleteComment(String slug, UUID commentId, String username) {
        ArticleEntity articleEntity = getArticleEntityBySlug(slug);
        CommentEntity commentEntity = commentMapper.getByCommentId(commentId);

        if(!articleEntity.getArticleId().equals(commentEntity.getArticleId()) ||
                !commentEntity.getUserId().equals(userMapper.findByUsername(username).getId())) {
            throw new UnauthorizedArticleModificationException(commentId.toString());
        }
        commentMapper.deleteByCommentId(commentId);
    }

    @Transactional
    public MultipleComments getComments(String slug, String username) {
        ArticleEntity articleEntity = getArticleEntityBySlug(slug);
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