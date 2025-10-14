package com.leafshop.controller;

import com.leafshop.entity.Cart;
import com.leafshop.service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Cart getCart(@RequestParam(required = false) Long userId,
                        @RequestParam(required = false) String sessionId) {
        return cartService.getCart(userId, sessionId);
    }

    @PostMapping("/add")
    public Cart addItem(@RequestParam(required = false) Long userId,
                        @RequestParam(required = false) String sessionId,
                        @RequestParam Long productId,
                        @RequestParam int quantity,
                        @RequestParam double price) {
        return cartService.addItem(userId, sessionId, productId, quantity, price);
    }

    @PutMapping("/update")
    public Cart updateItem(@RequestParam(required = false) Long userId,
                           @RequestParam(required = false) String sessionId,
                           @RequestParam Long productId,
                           @RequestParam int quantity) {
        return cartService.updateItem(userId, sessionId, productId, quantity);
    }

    @DeleteMapping("/remove")
    public Cart removeItem(@RequestParam(required = false) Long userId,
                           @RequestParam(required = false) String sessionId,
                           @RequestParam Long productId) {
        return cartService.removeItem(userId, sessionId, productId);
    }
}
