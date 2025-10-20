package com.leafshop.order.controller;

import com.leafshop.order.dto.*;
import com.leafshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrderFromCart(request));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrderHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrderHistory(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetail(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetail(orderId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request.getStatus()));
    }

    @PutMapping("/{orderId}/payment")
    public ResponseEntity<OrderResponse> updatePaymentStatus(
            @PathVariable Long orderId,
            @RequestBody UpdatePaymentStatusRequest request) {
        return ResponseEntity.ok(orderService.updatePaymentStatus(orderId, request.getPaymentStatus()));
    }

    @PutMapping("/{orderId}/assign/{staffId}")
    public ResponseEntity<OrderResponse> assignOrderToStaff(
            @PathVariable Long orderId,
            @PathVariable Long staffId) {
        return ResponseEntity.ok(orderService.assignOrderToStaff(orderId, staffId));
    }

    @PostMapping("/{orderId}/return")
    public ResponseEntity<OrderResponse> processReturn(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.processReturn(orderId));
    }
}
