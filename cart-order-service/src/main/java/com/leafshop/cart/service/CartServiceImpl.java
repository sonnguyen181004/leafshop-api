package com.leafshop.cart.service;

import com.leafshop.cart.entity.Cart;
import com.leafshop.cart.entity.CartItem;
import com.leafshop.cart.repository.CartItemRepository;
import com.leafshop.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository itemRepository;

    @Override
    public Cart getCart(Long userId, String sessionId) {
        return userId != null
                ? cartRepository.findByUserId(userId).orElseGet(() -> createCart(userId, null))
                : cartRepository.findBySessionId(sessionId).orElseGet(() -> createCart(null, sessionId));
    }

    private Cart createCart(Long userId, String sessionId) {
        Cart cart = Cart.builder()
                .userId(userId)
                .sessionId(sessionId)
                .totalPrice(0.0)
                .items(new ArrayList<>()) // ✅ luôn khởi tạo
                .build();
        return cartRepository.save(cart);
    }

    @Override
    public Cart addItem(Long userId, String sessionId, Long productId, String productName, Integer quantity, Double price) {
        Cart cart = getCart(userId, sessionId);

        CartItem existingItem = itemRepository.findByCartIdAndProductId(cart.getId(), productId).orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setProductName(productName);
            existingItem.setPrice(price);
            itemRepository.save(existingItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productId(productId)
                    .productName(productName)
                    .quantity(quantity)
                    .price(price)
                    .build();
            itemRepository.save(newItem);
            cart.getItems().add(newItem);
        }

        return calculateAndSave(cart);
    }

    @Override
    public Cart updateItem(Long userId, String sessionId, Long productId, Integer quantity) {
        Cart cart = getCart(userId, sessionId);

        CartItem item = itemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        item.setQuantity(quantity);
        itemRepository.save(item);

        return calculateAndSave(cart);
    }

    @Override
    public Cart removeItem(Long userId, String sessionId, Long productId) {
        Cart cart = getCart(userId, sessionId);

        CartItem item = itemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        itemRepository.delete(item);

        return calculateAndSave(cart);
    }

    @Override
    public double calculateTotal(Cart cart) {
        return itemRepository.findByCartId(cart.getId()).stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    private Cart calculateAndSave(Cart cart) {
        cart.setTotalPrice(calculateTotal(cart));
        return cartRepository.save(cart);
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for userId " + userId));
    }

    @Override
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        itemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cart.setTotalPrice(0.0);
        cartRepository.save(cart);
    }
}
