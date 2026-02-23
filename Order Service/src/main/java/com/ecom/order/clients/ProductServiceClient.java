package com.ecom.order.clients;

import com.ecom.order.dto.ProductResponse;
import com.ecom.order.dto.ReduceProductRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface ProductServiceClient {

    @GetExchange("/api/products/{id}")
    ProductResponse getProductDetails(@PathVariable String id);

    @PostExchange("/api/cart/reduce-stock")
    Boolean reduceProductStock(@RequestBody ReduceProductRequest reduceProductRequest);
}
