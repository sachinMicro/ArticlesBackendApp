package com.articles.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleResponseDTO {
	private Long id;
    private String title;
    private String content;
    private String author;
}
