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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Override
    @Transactional
    public OrderResponse createOrderFromCart(CreateOrderRequest request) {
        // 🔹 Kiểm tra đầu vào
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null!");
        }

        // 🔹 Lấy giỏ hàng hiện tại của user
        Cart cart = cartService.getCart(request.getUserId(), null);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty! Cannot create order.");
        }

        log.info("Creating order from cart for user {}", request.getUserId());

        // 🔹 Tạo entity Order
        Order order = Order.builder()
                .userId(request.getUserId())
                .totalAmount(cart.getTotalPrice() != null ? cart.getTotalPrice() : 0.0)
                .status("PENDING")
                .paymentStatus("UNPAID")
                .build();

        // 🔹 Copy từng CartItem sang OrderItem
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(ci -> OrderItem.builder()
                        .productId(ci.getProductId())
                        .productName(ci.getProductName())
                        .quantity(ci.getQuantity())
                        .price(ci.getPrice())
                        .order(order)
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);

        // 🔹 Lưu đơn hàng
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", savedOrder.getId());

        // 🔹 Xóa giỏ hàng sau khi checkout thành công
        try {
            cartService.clearCart(cart.getId());
            log.info("Cart {} cleared after checkout", cart.getId());
        } catch (Exception e) {
            log.warn("Failed to clear cart {} after checkout: {}", cart.getId(), e.getMessage());
        }

        // 🔹 Trả về response
        return toResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getOrderHistory(Long userId) {
        log.info("Fetching order history for user {}", userId);
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderDetail(Long orderId) {
        log.info("Fetching order detail for order {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        return toResponse(order);
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        log.info("Updating status of order {} to {}", orderId, status);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        order.setStatus(status);
        orderRepository.save(order);
        return toResponse(order);
    }

    @Override
    public OrderResponse updatePaymentStatus(Long orderId, String paymentStatus) {
        log.info("Updating payment status of order {} to {}", orderId, paymentStatus);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        order.setPaymentStatus(paymentStatus);
        orderRepository.save(order);
        return toResponse(order);
    }

    @Override
    public OrderResponse assignOrderToStaff(Long orderId, Long staffId) {
        log.info("Assigning order {} to staff {}", orderId, staffId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        order.setAssignedStaffId(staffId);
        orderRepository.save(order);
        return toResponse(order);
    }

    @Override
    public OrderResponse processReturn(Long orderId) {
        log.info("Processing return for order {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        order.setStatus("RETURNED");
        orderRepository.save(order);
        return toResponse(order);
    }

    //  Helper: Convert entity → DTO
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
