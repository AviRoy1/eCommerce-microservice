package com.ecom.order.service;


import com.ecom.order.dto.CartIteRequest;
import com.ecom.order.entity.CartItem;
import com.ecom.order.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;

    public boolean addToCart(String userId, CartIteRequest cartItemRequest) {
//        Product product = productRepository.findById(Long.valueOf(cartItemRequest.getProductId())).orElse(null);
//        if(product == null)
//            return false;
//        if(product.getStock() < cartItemRequest.getQuantity())
//            return false;
//
//        Users user = userRepository.findById(Long.valueOf(userId)).orElse(null);
//        if(null == user)
//            return false;
//
//        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId());
//        if(existingCartItem != null) {
//            // update quantity
//            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
//            BigDecimal totalPrice = BigDecimal.valueOf(existingCartItem.getQuantity()).multiply(product.getPrice());
//            existingCartItem.setPrice(totalPrice);
//            cartItemRepository.save(existingCartItem);
//        } else {
//            // add new cart item
//            CartItem cartItem = new CartItem();
//            cartItem.setUser(user);
//            cartItem.setProduct(product);
//            cartItem.setQuantity(cartItemRequest.getQuantity());
//            cartItem.setPrice(BigDecimal.valueOf(cartItemRequest.getQuantity()).multiply(product.getPrice()));
//            cartItemRepository.save(cartItem);
//        }
        return true;
    }

    public List<CartItem> findByUserId(String userId) {
        return cartItemRepository.findByUserId(userId);
    }
}
