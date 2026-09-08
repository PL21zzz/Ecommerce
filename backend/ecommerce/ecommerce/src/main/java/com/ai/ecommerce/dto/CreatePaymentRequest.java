package com.ai.ecommerce.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CreatePaymentRequest {
    private Long userId;
    private Double totalAmount;
    private Double deliveryFee;
    private String customerName;
    private String phone;
    private String address;
    private String note;
    private List<OrderItemRequest> items = new ArrayList<>();
}
