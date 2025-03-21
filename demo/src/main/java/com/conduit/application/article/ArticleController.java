package com.conduit.application.article;

import com.conduit.application.article.data.MultipleArticlesDTO;
import com.conduit.application.article.data.MultipleComments;
import com.conduit.application.article.data.SingleArticleDTO;
import com.conduit.application.article.data.SingleComment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<SingleArticleDTO> createArticle(@RequestBody @Valid SingleArticleDTO request, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(articleService.createArticle(request, user.getUsername()));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<SingleArticleDTO> updateArticle(@PathVariable String slug,
                                                          @RequestBody @Valid SingleArticleDTO request, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(articleService.updateArticle(slug, request, user.getUsername()));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<SingleArticleDTO> getArticle(@PathVariable String slug, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(articleService.getArticle(slug, user.getUsername()));
    }

    @PostMapping("/{slug}/favorite")
    public ResponseEntity<SingleArticleDTO> FavoriteArticle(@PathVariable String slug, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(articleService.favoriteArticle(slug, user.getUsername()));
    }

    @DeleteMapping("{slug}/favorite")
    public ResponseEntity<SingleArticleDTO> UnfavoriteArticle(@PathVariable String slug, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(articleService.unfavoriteArticle(slug, user.getUsername()));
    }

    @PostMapping("/{slug}/comments")
    public ResponseEntity<SingleComment> addComment(@PathVariable String slug,
                                                    @RequestBody @Valid SingleComment singleComment, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(articleService.addComment(slug, singleComment, user.getUsername()));
    }

    @DeleteMapping("/{slug}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String slug, @PathVariable UUID commentId, @AuthenticationPrincipal User user) {
        articleService.deleteComment(slug, commentId, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{slug}/comments")
    public ResponseEntity<MultipleComments> getComments(@PathVariable String slug, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(articleService.getComments(slug, user != null ? user.getUsername() : null));
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteArticle(@PathVariable String slug, @AuthenticationPrincipal User user) {
        articleService.deleteArticle(slug, user.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MultipleArticlesDTO> listArticles(
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "favorited", required = false) String favoritedBy,
            @RequestParam(value = "limit", required = false, defaultValue = "20") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok( articleService.listArticles(tag, author, favoritedBy, limit, offset, user != null ? user.getUsername() : null) );
    }
}