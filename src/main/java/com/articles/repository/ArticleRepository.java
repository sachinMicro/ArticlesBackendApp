package com.articles.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.articles.model.Article;
import com.articles.model.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
	 List<Article> findByAuthor(User author);
}
