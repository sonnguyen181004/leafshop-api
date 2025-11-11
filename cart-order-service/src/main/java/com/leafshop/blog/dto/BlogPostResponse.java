package com.leafshop.blog.dto;

import com.leafshop.blog.entity.BlogStatus;
import lombok.Data;

@Data
public class BlogPostResponse {
    private Long id;
    private String title;
    private String content;
    private String author;
    private BlogStatus status;

    
    private String createdAt;
}
