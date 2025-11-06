package com.leafshop.order.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private Long userId;
    private Double totalAmount;
    private String status;
    private String paymentStatus;
    private Long assignedStaffId;
    private LocalDateTime createdAt;
    private List<ItemResponse> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemResponse {
        private Long productId;
        private String productName;
        private Integer quantity;
        private Double price;
    }
}