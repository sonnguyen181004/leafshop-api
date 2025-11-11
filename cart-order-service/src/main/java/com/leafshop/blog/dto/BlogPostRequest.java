package com.leafshop.blog.dto;

import com.leafshop.blog.entity.BlogStatus;
import lombok.Data;

@Data
public class BlogPostRequest {
    private String title;
    private String content;
    private String author;
    private BlogStatus status;
}
