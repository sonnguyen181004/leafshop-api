package com.leafshop.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RemoveCartItemRequest {

    private Long userId;      // optional - nếu đã login
    private String sessionId; // optional - nếu chưa login

    @NotNull(message = "Product ID is required")
    private Long productId;
}
