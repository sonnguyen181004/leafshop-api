package com.leafshop.support.wishlist.controller;

import com.leafshop.support.wishlist.entity.WishlistItem;
import com.leafshop.support.wishlist.service.WishlistService;
import com.leafshop.support.wishlist.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // 🔹 Thêm sản phẩm vào wishlist
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToWishlist(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam String productName) {

        try {
            WishlistItem item = wishlistService.addToWishlist(userId, productId, productName);
            return ResponseEntity.ok(new ApiResponse(true, "Đã thêm sản phẩm vào wishlist"));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
        }
    }

    // 🔹 Lấy danh sách wishlist của user
    @GetMapping("/{userId}")
    public ResponseEntity<List<WishlistItem>> getWishlist(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistService.getWishlist(userId));
    }

    // 🔹 Xóa sản phẩm khỏi wishlist
    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse> removeFromWishlist(
            @RequestParam Long userId,
            @RequestParam Long productId) {

        boolean exists = wishlistService.isInWishlist(userId, productId);
        if (exists) {
            wishlistService.removeFromWishlist(userId, productId);
            return ResponseEntity.ok(new ApiResponse(true, "Đã xóa sản phẩm khỏi wishlist"));
        } else {
            return ResponseEntity.ok(new ApiResponse(false, "Sản phẩm không tồn tại trong wishlist"));
        }
    }

    // 🔹 Kiểm tra sản phẩm có trong wishlist
    @GetMapping("/check")
    public ResponseEntity<ApiResponse> isInWishlist(
            @RequestParam Long userId,
            @RequestParam Long productId) {

        boolean exists = wishlistService.isInWishlist(userId, productId);
        if (exists) {
            return ResponseEntity.ok(new ApiResponse(true, "Sản phẩm đã có trong wishlist"));
        } else {
            return ResponseEntity.ok(new ApiResponse(false, "Sản phẩm chưa có trong wishlist"));
        }
    }
}
