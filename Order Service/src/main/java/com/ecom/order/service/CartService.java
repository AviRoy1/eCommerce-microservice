package com.ecom.order.service;


import com.ecom.order.clients.ProductServiceClient;
import com.ecom.order.clients.UserServiceClient;
import com.ecom.order.dto.CartIteRequest;
import com.ecom.order.dto.ProductResponse;
import com.ecom.order.dto.ReduceProductRequest;
import com.ecom.order.dto.UserResponse;
import com.ecom.order.entity.CartItem;
import com.ecom.order.entity.Order;
import com.ecom.order.entity.OrderItem;
import com.ecom.order.repository.CartItemRepository;
import com.ecom.order.repository.OrderItemRepository;
import com.ecom.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public boolean addToCart(String userId, CartIteRequest cartItemRequest) {

        ProductResponse product = productServiceClient.getProductDetails(cartItemRequest.getProductId());
        if(product==null || product.getStock()<cartItemRequest.getQuantity())
            return false;

        UserResponse user = userServiceClient.findUserById(userId);
        if(user==null)
            return false;

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, cartItemRequest.getProductId());
        if(existingCartItem != null) {
            // update quantity
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemRequest.getQuantity());
            BigDecimal totalPrice = BigDecimal.valueOf(existingCartItem.getQuantity()).multiply(product.getPrice());
            existingCartItem.setPrice(totalPrice);
            cartItemRepository.save(existingCartItem);
        } else {
            // add new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUser(userId);
            cartItem.setProductId(cartItemRequest.getProductId());
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(cartItemRequest.getQuantity()).multiply(product.getPrice()));
            cartItemRepository.save(cartItem);
        }
        return true;
    }

    public List<CartItem> findByUserId(String userId) {
        return cartItemRepository.findByUserId(userId);
    }


}
