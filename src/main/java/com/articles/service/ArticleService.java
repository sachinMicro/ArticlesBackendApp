package com.articles.service;

import com.articles.dto.ArticleDTO;
import com.articles.model.Article;
import com.articles.model.User;
import com.articles.repository.ArticleRepository;
import com.articles.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public List<ArticleDTO> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ArticleDTO> getArticleById(Long id) {
        return articleRepository.findById(id).map(this::convertToDTO);
    }

    public ArticleDTO createArticle(ArticleDTO articleDTO, String username) {
        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Article article = Article.builder()
                .title(articleDTO.getTitle())
                .content(articleDTO.getContent())
                .author(author)
                .createdAt(LocalDateTime.now())
                .build();

        return convertToDTO(articleRepository.save(article));
    }

    public Optional<ArticleDTO> updateArticle(Long id, ArticleDTO articleDTO, String username) {
        return articleRepository.findById(id)
                .filter(article -> article.getAuthor().getUsername().equals(username))
                .map(article -> {
                    article.setTitle(articleDTO.getTitle());
                    article.setContent(articleDTO.getContent());
                    return convertToDTO(articleRepository.save(article));
                });
    }

    public boolean deleteArticle(Long id, String username) {
        Optional<Article> article = articleRepository.findById(id);
        if (article.isPresent() && article.get().getAuthor().getUsername().equals(username)) {
            articleRepository.deleteById(id);
            return true;
        }
        throw new AccessDeniedException("You are not allowed to delete this article");
    }

    private ArticleDTO convertToDTO(Article article) {
        return new ArticleDTO(article.getId(), article.getTitle(), article.getContent(), article.getAuthor().getUsername(), article.getCreatedAt());
    }
}
