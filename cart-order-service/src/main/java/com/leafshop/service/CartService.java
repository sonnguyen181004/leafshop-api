package com.leafshop.service;

import com.leafshop.entity.Cart;
import com.leafshop.entity.CartItem;


public interface CartService {
    Cart getCart(Long userId, String sessionId);
    Cart addItem(Long userId, String sessionId, Long productId, int quantity, double price);
    Cart updateItem(Long userId, String sessionId, Long productId, int quantity);
    Cart removeItem(Long userId, String sessionId, Long productId);
    double calculateTotal(Cart cart);
}
