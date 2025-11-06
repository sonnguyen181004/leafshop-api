package com.leafshop.coupon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_order_coupons") // nhất quán với tbl_orders
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID đơn hàng mà coupon được áp dụng
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    // Liên kết coupon được dùng
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    // Số tiền được giảm
    @Column(name = "discount_applied", nullable = false)
    private Double discountApplied;

    // Thêm timestamp nếu bạn muốn tracking
    @Column(name = "applied_at", nullable = false, updatable = false)
    private java.time.LocalDateTime appliedAt;

    @PrePersist
    public void prePersist() {
        if (appliedAt == null) {
            appliedAt = java.time.LocalDateTime.now();
        }
        if (discountApplied == null) {
            discountApplied = 0.0;
        }
    }
}
