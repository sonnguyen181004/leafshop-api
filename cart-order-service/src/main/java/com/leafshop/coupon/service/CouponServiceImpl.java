package com.leafshop.coupon.service;

import com.leafshop.coupon.dto.*;
import com.leafshop.coupon.entity.*;
import com.leafshop.coupon.repository.*;
import com.leafshop.order.entity.Order; // ✅ thêm import này
import com.leafshop.order.repository.OrderRepository; // ✅ thêm import này
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final OrderCouponRepository orderCouponRepository;
    private final OrderRepository orderRepository; // ✅ thêm dependency để truy cập order

    @Override
    public CouponResponse createCoupon(CouponRequest req) {
        validateDate(req.getStartDate(), req.getEndDate());

        Coupon coupon = Coupon.builder()
                .code(req.getCode().trim().toUpperCase())
                .type(req.getType().trim().toUpperCase())
                .discountValue(req.getDiscountValue())
                .usageLimit(req.getUsageLimit())
                .startDate(req.getStartDate())
                .endDate(req.getEndDate())
                .active(true)
                .usedCount(0)
                .build();

        couponRepository.save(coupon);
        return mapToResponse(coupon);
    }

    @Override
    public CouponResponse updateCoupon(Long id, CouponRequest req) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));

        validateDate(req.getStartDate(), req.getEndDate());

        coupon.setCode(req.getCode().trim().toUpperCase());
        coupon.setType(req.getType().trim().toUpperCase());
        coupon.setDiscountValue(req.getDiscountValue());
        coupon.setUsageLimit(req.getUsageLimit());
        coupon.setStartDate(req.getStartDate());
        coupon.setEndDate(req.getEndDate());

        couponRepository.save(coupon);
        return mapToResponse(coupon);
    }

    @Override
    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new RuntimeException("Coupon not found to delete");
        }
        couponRepository.deleteById(id);
    }

    @Override
    public List<CouponResponse> getAllCoupons() {
        return couponRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CouponResponse getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code.trim().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Coupon not found with code: " + code));
        return mapToResponse(coupon);
    }

@Override
public Double applyCoupon(ApplyCouponRequest request) {
    Coupon coupon = couponRepository.findByCode(request.getCouponCode().trim().toUpperCase())
            .orElseThrow(() -> new RuntimeException("Invalid coupon code"));

    if (!coupon.isValid()) {
        throw new RuntimeException("Coupon expired, inactive or usage limit exceeded");
    }

    // ✅ Lấy order từ DB để cập nhật lại totalAmount
    Order order = orderRepository.findById(request.getOrderId())
            .orElseThrow(() -> new RuntimeException("Order not found"));

    double orderTotal = order.getTotalAmount();

    // ✅ Tính discount theo loại mã
    double discount = switch (coupon.getType().toUpperCase()) {
        case "PERCENTAGE" -> orderTotal * (coupon.getDiscountValue() / 100);
        case "FIXED" -> coupon.getDiscountValue();
        case "FREESHIP" -> 30000.0;
        default -> throw new RuntimeException("Unsupported coupon type");
    };

    // ✅ Cập nhật lại totalAmount (không âm)
    double newTotal = Math.max(orderTotal - discount, 0);
    order.setTotalAmount(newTotal);
    orderRepository.save(order);

    // ✅ Tăng lượt sử dụng
    coupon.incrementUsage();
    couponRepository.save(coupon);

    // ✅ Ghi nhận coupon áp dụng cho đơn hàng
    orderCouponRepository.save(OrderCoupon.builder()
            .orderId(order.getId())
            .coupon(coupon)
            .discountApplied(discount)
            .build());

    return discount;
}

    /** ✅ Validate date logic **/
    private void validateDate(LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();

        if (startDate == null || endDate == null) {
            throw new RuntimeException("Start date and end date cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("Start date must be before end date");
        }
        if (endDate.isBefore(now)) {
            throw new RuntimeException("End date must be after the current date");
        }
    }

    /** ✅ Convert Entity to DTO **/
    private CouponResponse mapToResponse(Coupon c) {
        return CouponResponse.builder()
                .id(c.getId())
                .code(c.getCode())
                .type(c.getType())
                .discountValue(c.getDiscountValue())
                .usageLimit(c.getUsageLimit())
                .usedCount(c.getUsedCount())
                .active(c.getActive())
                .startDate(c.getStartDate())
                .endDate(c.getEndDate())
                .build();
    }
}
