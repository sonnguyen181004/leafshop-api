package com.leafshop.support.staff.service;

import com.leafshop.order.dto.OrderResponse;
import com.leafshop.order.service.OrderService;
import com.leafshop.user.entity.User;
import com.leafshop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final UserRepository userRepository;
    private final OrderService orderService;

    @Override
    public List<User> getAllCustomers() {
        // Lấy tất cả user có role = CUSTOMER
        return userRepository.findAll()
                .stream()
                .filter(u -> "CUSTOMER".equals(u.getRole()))
                .toList();
    }

    @Override
    public List<OrderResponse> getCustomerOrderHistory(Long userId) {
        return orderService.getOrderHistory(userId);
    }
}
