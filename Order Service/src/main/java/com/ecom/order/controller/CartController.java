package com.ecom.order.controller;

import com.ecom.order.dto.CartIteRequest;
import com.ecom.order.entity.CartItem;
import com.ecom.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") String userId,
                                          @RequestBody CartIteRequest cartItemRequest) {
        boolean isItemAdded = cartService.addToCart(userId, cartItemRequest);
        if(!isItemAdded) {
            return ResponseEntity.badRequest().body("Product Out od Stock or User not found!!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItemForUser(@RequestHeader("X-User-ID") String userId) {
        List<CartItem> cartItem = cartService.findByUserId(userId);
        return ResponseEntity.ok(cartItem);
    }

}
