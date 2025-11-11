package com.leafshop.support.staff.controller;

import com.leafshop.order.dto.OrderResponse;
import com.leafshop.user.entity.User;
import com.leafshop.support.staff.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    // Lấy danh sách tất cả khách hàng
    @GetMapping("/customers")
    public ResponseEntity<List<User>> getAllCustomers() {
        return ResponseEntity.ok(staffService.getAllCustomers());
    }

    // Lấy lịch sử mua hàng của khách
    @GetMapping("/customers/{userId}/orders")
    public ResponseEntity<List<OrderResponse>> getCustomerOrderHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(staffService.getCustomerOrderHistory(userId));
    }
}
