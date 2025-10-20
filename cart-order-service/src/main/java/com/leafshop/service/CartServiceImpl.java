package com.leafshop.service;

import com.leafshop.entity.Cart;
import com.leafshop.entity.CartItem;
import com.leafshop.repository.CartItemRepository;
import com.leafshop.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
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
        Cart cart = Cart.builder()
                .userId(userId)
                .sessionId(sessionId)
                .totalPrice(0.0)
                .build();
        return cartRepository.save(cart);
    }

    @Override
public Cart addItem(Long userId, String sessionId, Long productId, int quantity, double price) {
    Cart cart = getCart(userId, sessionId);

    CartItem existingItem = itemRepository.findByCartIdAndProductId(cart.getId(), productId)
            .orElse(null);

    if (existingItem != null) {
        existingItem.setQuantity(existingItem.getQuantity() + quantity);
    } else {
        CartItem newItem = CartItem.builder()
                .cart(cart)
                .productId(productId)
                .quantity(quantity)
                .price(price)
                .build();

        
        cart.getItems().add(newItem);
    }

    return calculateAndSave(cart);
}

    @Override
    public Cart updateItem(Long userId, String sessionId, Long productId, int quantity) {
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
}
