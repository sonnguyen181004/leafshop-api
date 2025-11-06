package com.leafshop.coupon.repository;

import com.leafshop.coupon.entity.OrderCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderCouponRepository extends JpaRepository<OrderCoupon, Long> {
    List<OrderCoupon> findByOrderId(Long orderId);
}
