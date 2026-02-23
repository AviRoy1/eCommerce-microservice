package com.ecom.order.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrderRequest {
    private List<String> cartIds;
}
