package com.ecom.product.controller;

import com.ecom.product.dto.ProductRequest;
import com.ecom.product.dto.ProductResponse;
import com.ecom.product.dto.ReduceProductRequest;
import com.ecom.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return  new ResponseEntity<ProductResponse>(productService.createProduct(productRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        return  new ResponseEntity<ProductResponse>(productService.updateProduct(id, productRequest), HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findProductById(@PathVariable Long id) {
        return  new ResponseEntity<ProductResponse>(productService.findProductById(id), HttpStatus.CREATED);
    }

    @PostMapping("/reduce-stock")
    public ResponseEntity<Boolean> reduceProductStock(@RequestBody ReduceProductRequest request) {
        return new ResponseEntity<Boolean>(productService.reduceProductStock(request), HttpStatus.ACCEPTED);
    }

}
