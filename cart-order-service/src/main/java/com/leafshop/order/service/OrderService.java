package com.leafshop.order.service;

import com.leafshop.order.dto.CreateOrderRequest;
import com.leafshop.order.dto.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse createOrderFromCart(CreateOrderRequest request);

    List<OrderResponse> getOrderHistory(Long userId);

    OrderResponse getOrderDetail(Long orderId);

    OrderResponse updateOrderStatus(Long orderId, String status);

    OrderResponse updatePaymentStatus(Long orderId, String paymentStatus);

    OrderResponse assignOrderToStaff(Long orderId, Long staffId);

    OrderResponse processReturn(Long orderId);
}