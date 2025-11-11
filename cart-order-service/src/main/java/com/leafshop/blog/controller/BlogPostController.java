package com.leafshop.blog.controller;

import com.leafshop.blog.dto.BlogPostRequest;
import com.leafshop.blog.dto.BlogPostResponse;
import com.leafshop.blog.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    // --- CRUD cho Manager/Staff ---
    @PostMapping
    public ResponseEntity<BlogPostResponse> create(@RequestBody BlogPostRequest req) {
        return ResponseEntity.ok(blogPostService.createPost(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogPostResponse> update(@PathVariable Long id, @RequestBody BlogPostRequest req) {
        return ResponseEntity.ok(blogPostService.updatePost(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        blogPostService.deletePost(id);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping("/admin")
    public ResponseEntity<List<BlogPostResponse>> listForAdmin() {
        return ResponseEntity.ok(blogPostService.getAllForManager());
    }

    // --- API cho khách hàng ---
    @GetMapping
    public ResponseEntity<List<BlogPostResponse>> listForCustomer() {
        return ResponseEntity.ok(blogPostService.getPublishedForCustomer());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogPostResponse> detail(@PathVariable Long id) {
        return ResponseEntity.ok(blogPostService.getDetail(id));
    }
}
