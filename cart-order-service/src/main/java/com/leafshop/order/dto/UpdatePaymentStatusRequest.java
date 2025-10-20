package com.leafshop.order.dto;

import lombok.Data;

@Data
public class UpdatePaymentStatusRequest {
    private String paymentStatus;
}
