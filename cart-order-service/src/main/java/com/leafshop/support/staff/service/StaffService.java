package com.leafshop.support.staff.service;

import com.leafshop.order.dto.OrderResponse;
import com.leafshop.user.entity.User;

import java.util.List;

public interface StaffService {
    // Lấy danh sách khách hàng (tất cả user role = CUSTOMER)
    List<User> getAllCustomers();

    // Lấy lịch sử đơn hàng của 1 khách
    List<OrderResponse> getCustomerOrderHistory(Long userId);
}
