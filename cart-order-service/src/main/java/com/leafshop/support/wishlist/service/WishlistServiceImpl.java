package com.leafshop.support.wishlist.service;

import com.leafshop.support.wishlist.entity.WishlistItem;
import com.leafshop.support.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public WishlistItem addToWishlist(Long userId, Long productId, String productName) {
        // Kiểm tra đã có chưa
        if (wishlistRepository.findByUserIdAndProductId(userId, productId).isPresent()) {
            throw new RuntimeException("Product already in wishlist");
        }

        WishlistItem item = WishlistItem.builder()
                .userId(userId)
                .productId(productId)
                .productName(productName)
                .build();

        return wishlistRepository.save(item);
    }

    @Override
    public List<WishlistItem> getWishlist(Long userId) {
        return wishlistRepository.findByUserIdOrderByAddedAtDesc(userId);
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {
        wishlistRepository.findByUserIdAndProductId(userId, productId)
                .ifPresent(wishlistRepository::delete);
    }

    @Override
    public boolean isInWishlist(Long userId, Long productId) {
        return wishlistRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }
}
