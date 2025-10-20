package com.leafshop.cart.service;

import com.leafshop.cart.entity.Cart;

public interface CartService {

    Cart getCart(Long userId, String sessionId);

    // ✅ Đổi int → Integer, double → Double
    Cart addItem(Long userId, String sessionId, Long productId, String productName, Integer quantity, Double price);

    Cart updateItem(Long userId, String sessionId, Long productId, Integer quantity);

    Cart removeItem(Long userId, String sessionId, Long productId);

    double calculateTotal(Cart cart);

    Cart getCartByUserId(Long userId);

    void clearCart(Long cartId);
}
