package com.ecom.product.dto;

import lombok.Data;

@Data
public class ReduceProductRequest {
    private String productId;
    private Integer quantity;
}
