package com.leafshop.blog.repository;

import com.leafshop.blog.entity.BlogPost;
import com.leafshop.blog.entity.BlogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByStatus(BlogStatus status);
}
