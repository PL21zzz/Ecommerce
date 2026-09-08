package com.ai.ecommerce.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long productId;
    private String productTitle;
    private String image;
    private String size;
    private Double unitPrice;
    private Integer quantity;
}
