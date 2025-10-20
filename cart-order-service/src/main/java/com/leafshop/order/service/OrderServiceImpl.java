package com.leafshop.order.service;

import com.leafshop.cart.entity.Cart;
import com.leafshop.cart.entity.CartItem;
import com.leafshop.cart.service.CartService;
import com.leafshop.order.dto.CreateOrderRequest;
import com.leafshop.order.dto.OrderResponse;
import com.leafshop.order.entity.Order;
import com.leafshop.order.entity.OrderItem;
import com.leafshop.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Override
    @Transactional
    public OrderResponse createOrderFromCart(CreateOrderRequest request) {
        Cart cart = cartService.getCart(request.getUserId(), null);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty!");
        }

        Order order = Order.builder()
                .userId(request.getUserId())
                .totalAmount(cart.getTotalPrice())
                .status("PENDING")
                .paymentStatus("UNPAID")
                .build();

        // ✅ Copy đầy đủ thông tin product từ Cart sang Order
        List<OrderItem> orderItems = cart.getItems().stream()
                .map((CartItem ci) -> OrderItem.builder()
                        .productId(ci.getProductId())
                        .productName(ci.getProductName()) 
                        .quantity(ci.getQuantity())
                        .price(ci.getPrice())
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);
        orderRepository.save(order);

        // ✅ Xóa giỏ hàng sau khi tạo đơn
        cartService.clearCart(cart.getId());

        return toResponse(order);
    }

    @Override
    public List<OrderResponse> getOrderHistory(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return toResponse(order);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
        return toResponse(order);
    }

    @Override
    public OrderResponse updatePaymentStatus(Long orderId, String paymentStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setPaymentStatus(paymentStatus);
        orderRepository.save(order);
        return toResponse(order);
    }

    @Override
    public OrderResponse assignOrderToStaff(Long orderId, Long staffId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setAssignedStaffId(staffId);
        orderRepository.save(order);
        return toResponse(order);
    }

    @Override
    public OrderResponse processReturn(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus("RETURNED");
        orderRepository.save(order);
        return toResponse(order);
    }

    // ✅ Bổ sung hiển thị productName trong response
    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .paymentStatus(order.getPaymentStatus())
                .assignedStaffId(order.getAssignedStaffId())
                .createdAt(order.getCreatedAt())
                .items(order.getItems() == null ? List.of() :
                        order.getItems().stream()
                                .map(oi -> OrderResponse.ItemResponse.builder()
                                        .productId(oi.getProductId())
                                        .productName(oi.getProductName()) 
                                        .quantity(oi.getQuantity())
                                        .price(oi.getPrice())
                                        .build())
                                .collect(Collectors.toList()))
                .build();
    }
}
