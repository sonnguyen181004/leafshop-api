package com.leafshop.coupon.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Coupon code is required")
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @NotBlank(message = "Type is required")
    @Column(nullable = false, length = 20)
    private String type; // PERCENTAGE, FIXED, FREESHIP

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.0", message = "Discount value must be >= 0")
    @Column(nullable = false)
    private Double discountValue;

    @NotNull(message = "Usage limit is required")
    @Min(value = 1, message = "Usage limit must be at least 1")
    @Column(nullable = false)
    private Integer usageLimit;

    @Builder.Default
    @Column(nullable = false)
    private Integer usedCount = 0;

    @NotNull(message = "Start date is required")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(nullable = false)
    private LocalDate endDate;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @PrePersist
    public void prePersist() {
        if (usedCount == null) usedCount = 0;
        if (active == null) active = true;
    }

    public boolean isValid() {
        LocalDate now = LocalDate.now();
        return Boolean.TRUE.equals(active)
                && (now.isEqual(startDate) || now.isAfter(startDate))
                && (now.isBefore(endDate) || now.isEqual(endDate))
                && usedCount < usageLimit;
    }

    public void incrementUsage() {
        if (usedCount < usageLimit) {
            usedCount++;
        } else {
            throw new IllegalStateException("Coupon usage limit exceeded");
        }
    }
}
