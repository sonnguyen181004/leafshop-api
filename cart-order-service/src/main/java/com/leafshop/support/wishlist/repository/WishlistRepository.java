package com.leafshop.support.wishlist.repository;

import com.leafshop.support.wishlist.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUserIdOrderByAddedAtDesc(Long userId);

    Optional<WishlistItem> findByUserIdAndProductId(Long userId, Long productId);
}
