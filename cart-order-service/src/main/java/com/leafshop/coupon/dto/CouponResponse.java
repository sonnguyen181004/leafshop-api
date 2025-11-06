package com.leafshop.coupon.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponse {
    private Long id;
    private String code;
    private String type;
    private Double discountValue;
    private Integer usageLimit;
    private Integer usedCount;
    private Boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
}
