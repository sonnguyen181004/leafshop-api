package com.leafshop.coupon.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyCouponRequest {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotBlank(message = "Coupon code is required")
    private String couponCode;

    @NotNull(message = "Order total cannot be null")
    @DecimalMin(value = "0.0", message = "Order total must be >= 0")
    private Double orderTotal;
}
