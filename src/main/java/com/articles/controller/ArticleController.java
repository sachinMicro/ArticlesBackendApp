package com.articles.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.articles.dto.ArticleDTO;
import com.articles.service.ArticleService;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        return ResponseEntity.ok(articleService.getAllArticles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable("id") Long id) {
        Optional<ArticleDTO> article = articleService.getArticleById(id);
        return article.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody ArticleDTO articleDTO, Authentication authentication) {
        return ResponseEntity.ok(articleService.createArticle(articleDTO, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable("id") Long id, 
                                                    @RequestBody ArticleDTO articleDTO, 
                                                    Authentication authentication) {
        if (!isArticleOwner(id, authentication.getName())) {
            return ResponseEntity.status(403).build();
        }

        return articleService.updateArticle(id, articleDTO, authentication.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") Long id, Authentication authentication) {
        if (!isArticleOwner(id, authentication.getName())) {
            return ResponseEntity.status(403).build();
        }
        return articleService.deleteArticle(id, authentication.getName()) ? 
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private boolean isArticleOwner(Long id, String username) {
        Optional<ArticleDTO> article = articleService.getArticleById(id);
        return article.isPresent() && article.get().getAuthor().equals(username);
    }
}
