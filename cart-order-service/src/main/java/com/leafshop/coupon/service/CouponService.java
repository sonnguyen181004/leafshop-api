package com.leafshop.coupon.service;

import com.leafshop.coupon.dto.*;
import java.util.List;

public interface CouponService {
    CouponResponse createCoupon(CouponRequest request);
    CouponResponse updateCoupon(Long id, CouponRequest request);
    void deleteCoupon(Long id);
    List<CouponResponse> getAllCoupons();
    CouponResponse getCouponByCode(String code);
    Double applyCoupon(ApplyCouponRequest request);
}
