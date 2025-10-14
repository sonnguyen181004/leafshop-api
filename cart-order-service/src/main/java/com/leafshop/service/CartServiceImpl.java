package com.leafshop.service;

import com.leafshop.entity.*;
import com.leafshop.repository.*;
import org.springframework.stereotype.Service;


@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository itemRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository itemRepository) {
        this.cartRepository = cartRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Cart getCart(Long userId, String sessionId) {
        return userId != null
                ? cartRepository.findByUserId(userId).orElseGet(() -> createCart(userId, null))
                : cartRepository.findBySessionId(sessionId).orElseGet(() -> createCart(null, sessionId));
    }

    private Cart createCart(Long userId, String sessionId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setSessionId(sessionId);
        cart.setTotalPrice(0.0);
        return cartRepository.save(cart);
    }

    @Override
    public Cart addItem(Long userId, String sessionId, Long productId, int quantity, double price) {
        Cart cart = getCart(userId, sessionId);
        CartItem item = new CartItem();
        item.setProductId(productId);
        item.setQuantity(quantity);
        item.setPrice(price);
        item.setCart(cart);
        itemRepository.save(item);
        return calculateAndSave(cart);
    }

    @Override
    public Cart updateItem(Long userId, String sessionId, Long productId, int quantity) {
        Cart cart = getCart(userId, sessionId);
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                itemRepository.save(item);
            }
        }
        return calculateAndSave(cart);
    }

    @Override
    public Cart removeItem(Long userId, String sessionId, Long productId) {
        Cart cart = getCart(userId, sessionId);
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        return calculateAndSave(cart);
    }

    @Override
    public double calculateTotal(Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    private Cart calculateAndSave(Cart cart) {
        cart.setTotalPrice(calculateTotal(cart));
        return cartRepository.save(cart);
    }
}
