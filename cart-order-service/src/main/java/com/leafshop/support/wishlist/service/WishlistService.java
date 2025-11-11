package com.leafshop.support.wishlist.service;

import com.leafshop.support.wishlist.entity.WishlistItem;

import java.util.List;

public interface WishlistService {

    WishlistItem addToWishlist(Long userId, Long productId, String productName);

    List<WishlistItem> getWishlist(Long userId);

    void removeFromWishlist(Long userId, Long productId);

    boolean isInWishlist(Long userId, Long productId);
}
