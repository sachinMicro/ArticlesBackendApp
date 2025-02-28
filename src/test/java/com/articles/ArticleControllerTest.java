package com.articles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.articles.controller.ArticleController;
import com.articles.dto.ArticleDTO;
import com.articles.service.ArticleService;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @Mock
    private ArticleService articleService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ArticleController articleController;

    private ArticleDTO articleDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        articleDTO = new ArticleDTO(1L, "Test Title", "Test Conwtent", "testUser", LocalDateTime.now());
    }

    @Test
    void testGetAllArticles() {
        when(articleService.getAllArticles()).thenReturn(List.of(articleDTO));
        ResponseEntity<List<ArticleDTO>> response = articleController.getAllArticles();
        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetArticleById() {
        when(articleService.getArticleById(1L)).thenReturn(Optional.of(articleDTO));
        ResponseEntity<ArticleDTO> response = articleController.getArticleById(1L);
        assertEquals(200,response.getStatusCode().value());
        assertNotNull(response.getBody());
        
    }


    @Test
    void testCreateArticle() {
        when(authentication.getName()).thenReturn("testUser");
        when(articleService.createArticle(any(ArticleDTO.class), eq("testUser"))).thenReturn(articleDTO);
        ResponseEntity<ArticleDTO> response = articleController.createArticle(articleDTO, authentication);
        assertEquals(200,response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Test Title", response.getBody().getTitle());
    }

    @Test
    void testUpdateArticle() {
        when(authentication.getName()).thenReturn("testUser");
        when(articleService.updateArticle(eq(1L), any(ArticleDTO.class), eq("testUser"))).thenReturn(Optional.of(articleDTO));
        ResponseEntity<ArticleDTO> response = articleController.updateArticle(1L, articleDTO, authentication);
        assertEquals(200,response.getStatusCode().value());
        assertNotNull(response.getBody());
    }


    @Test
    void testDeleteArticle() {
        when(authentication.getName()).thenReturn("testUser");
        when(articleService.deleteArticle(1L, "testUser")).thenReturn(true);

        ResponseEntity<Void> response = articleController.deleteArticle(1L, authentication);

        assertEquals(204, response.getStatusCode().value());
    }


}