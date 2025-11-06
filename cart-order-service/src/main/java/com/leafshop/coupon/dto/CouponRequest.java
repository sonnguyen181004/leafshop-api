package com.leafshop.coupon.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

/**
 * DTO for creating or updating a coupon.
 * Validates required fields and ensures proper discount and date ranges.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponRequest {

    @NotBlank(message = "Coupon code is required")
    private String code;

    @NotBlank(message = "Coupon type is required (e.g. PERCENTAGE, FIXED, FREESHIP)")
    private String type;

    @NotNull(message = "Discount value cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount value must be greater than 0")
    private Double discountValue;

    @NotNull(message = "Usage limit cannot be null")
    @Min(value = 1, message = "Usage limit must be at least 1")
    private Integer usageLimit;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be today or in the future")
    @JsonFormat(pattern = "yyyy-MM-dd") // ✅ Bắt buộc để parse đúng định dạng JSON
    private LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
