package com.ecom.order.dto;

import lombok.Data;

@Data
public class CartIteRequest {
    private String productId;
    private Integer quantity;
//    private BigDecimal price;
}
