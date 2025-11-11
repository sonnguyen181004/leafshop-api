package com.leafshop.blog.service;

import com.leafshop.blog.dto.BlogPostRequest;
import com.leafshop.blog.dto.BlogPostResponse;
import com.leafshop.blog.entity.BlogPost;
import com.leafshop.blog.entity.BlogStatus;
import com.leafshop.blog.repository.BlogPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;

    // formatter để hiển thị ngày/tháng/năm
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BlogPostResponse createPost(BlogPostRequest req) {
        BlogPost post = BlogPost.builder()
                .title(req.getTitle())
                .content(req.getContent())
                .author(req.getAuthor())
                .status(req.getStatus() != null ? req.getStatus() : BlogStatus.DRAFT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return toResponse(blogPostRepository.save(post));
    }

    public BlogPostResponse updatePost(Long id, BlogPostRequest req) {
        BlogPost post = blogPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog post not found"));

        post.setTitle(req.getTitle());
        post.setContent(req.getContent());
        post.setAuthor(req.getAuthor());
        post.setStatus(req.getStatus());
        post.setUpdatedAt(LocalDateTime.now());

        return toResponse(blogPostRepository.save(post));
    }

    public void deletePost(Long id) {
        blogPostRepository.deleteById(id);
    }

    public List<BlogPostResponse> getAllForManager() {
        return blogPostRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<BlogPostResponse> getPublishedForCustomer() {
        return blogPostRepository.findByStatus(BlogStatus.PUBLISHED)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public BlogPostResponse getDetail(Long id) {
        return blogPostRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
    }

    private BlogPostResponse toResponse(BlogPost post) {
        BlogPostResponse res = new BlogPostResponse();
        res.setId(post.getId());
        res.setTitle(post.getTitle());
        res.setContent(post.getContent());
        res.setAuthor(post.getAuthor());
        res.setStatus(post.getStatus());
        // Format createdAt thành dd/MM/yyyy
        res.setCreatedAt(post.getCreatedAt().format(DATE_FORMATTER));
        return res;
    }
}
