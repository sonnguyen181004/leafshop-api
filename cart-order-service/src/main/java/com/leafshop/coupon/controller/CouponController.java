package com.leafshop.coupon.controller;

import com.leafshop.coupon.dto.*;
import com.leafshop.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> create(@RequestBody CouponRequest req) {
        return ResponseEntity.ok(couponService.createCoupon(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponResponse> update(@PathVariable Long id, @RequestBody CouponRequest req) {
        return ResponseEntity.ok(couponService.updateCoupon(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> list() {
        return ResponseEntity.ok(couponService.getAllCoupons());
    }

    @GetMapping("/{code}")
    public ResponseEntity<CouponResponse> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(couponService.getCouponByCode(code));
    }

    @PostMapping("/apply")
    public ResponseEntity<Double> apply(@RequestBody ApplyCouponRequest req) {
        return ResponseEntity.ok(couponService.applyCoupon(req));
    }
}
