package com.leafshop.cart.controller;

import com.leafshop.cart.dto.AddToCartRequest;
import com.leafshop.cart.dto.UpdateCartItemRequest;
import com.leafshop.cart.dto.RemoveCartItemRequest;
import com.leafshop.cart.dto.ApiResponse;
import com.leafshop.cart.entity.Cart;
import com.leafshop.cart.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // ✅ Lấy giỏ hàng theo userId hoặc sessionId
    @GetMapping
    public ResponseEntity<ApiResponse<Cart>> getCart(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String sessionId) {

        if (userId == null && (sessionId == null || sessionId.isEmpty())) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "userId or sessionId is required", null));
        }

        Cart cart = cartService.getCart(userId, sessionId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cart retrieved successfully", cart));
    }

    // ✅ Thêm sản phẩm vào giỏ
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Cart>> addItem(@Valid @RequestBody AddToCartRequest request) {
        Cart cart = cartService.addItem(
                request.getUserId(),
                request.getSessionId(),
                request.getProductId(),
                request.getProductName(),  // ✅ bổ sung dòng này
                request.getQuantity(),
                request.getPrice()
        );

        return ResponseEntity.ok(new ApiResponse<>(true, "Item added to cart", cart));
    }

    // ✅ Cập nhật số lượng sản phẩm
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Cart>> updateItem(@Valid @RequestBody UpdateCartItemRequest request) {
        Cart cart = cartService.updateItem(
                request.getUserId(),
                request.getSessionId(),
                request.getProductId(),
                request.getQuantity()
        );

        return ResponseEntity.ok(new ApiResponse<>(true, "Cart item updated", cart));
    }

    // ✅ Xóa sản phẩm khỏi giỏ
    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Cart>> removeItem(@Valid @RequestBody RemoveCartItemRequest request) {
        Cart cart = cartService.removeItem(
                request.getUserId(),
                request.getSessionId(),
                request.getProductId()
        );
        return ResponseEntity.ok(new ApiResponse<>(true, "Item removed from cart", cart));
    }

    // ✅ API tính tổng tiền
    @GetMapping("/total")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getCartTotal(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String sessionId) {

        if (userId == null && (sessionId == null || sessionId.isEmpty())) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "userId or sessionId is required", null));
        }

        Cart cart = cartService.getCart(userId, sessionId);
        double subtotal = cartService.calculateTotal(cart);
        double shippingFee = (subtotal >= 500000) ? 0 : 30000;
        double discount = 0;
        double grandTotal = subtotal + shippingFee - discount;

        Map<String, Double> totals = Map.of(
                "subtotal", subtotal,
                "shippingFee", shippingFee,
                "discount", discount,
                "grandTotal", grandTotal
        );

        return ResponseEntity.ok(new ApiResponse<>(true, "Cart total calculated successfully", totals));
    }

    // ✅ API checkout
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkout(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String sessionId) {

        if (userId == null && (sessionId == null || sessionId.isEmpty())) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "userId or sessionId is required", null));
        }

        Cart cart = cartService.getCart(userId, sessionId);
        if (cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(false, "Cart is empty", null));
        }

        double subtotal = cartService.calculateTotal(cart);
        double shippingFee = (subtotal >= 500000) ? 0 : 30000;
        double discount = 0;
        double grandTotal = subtotal + shippingFee - discount;

        Map<String, Object> orderSummary = Map.of(
                "orderId", "TEMP-" + System.currentTimeMillis(),
                "subtotal", subtotal,
                "shippingFee", shippingFee,
                "discount", discount,
                "grandTotal", grandTotal,
                "itemCount", cart.getItems().size()
        );

        // ✅ Dọn giỏ hàng sau khi checkout
        cartService.clearCart(cart.getId());

        return ResponseEntity.ok(new ApiResponse<>(true, "Checkout successful", orderSummary));
    }
}
