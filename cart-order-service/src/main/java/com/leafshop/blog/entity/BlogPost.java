package com.leafshop.blog.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blog_posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Content cannot be empty")
    @Lob
    private String content;

    @Size(max = 255)
    private String author;

    @Enumerated(EnumType.STRING)
    private BlogStatus status; // DRAFT or PUBLISHED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
