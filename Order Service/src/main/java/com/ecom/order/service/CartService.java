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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

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

    public @Nullable Boolean createOrder(String userId) {
        UserResponse user = userServiceClient.findUserById(userId);
        if(user == null){
            throw new RuntimeException("User not found!!");
        }

        List<CartItem> allCartItems = findByUserId(userId);
        if(allCartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Please add items to order!!");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for(CartItem cartItem: allCartItems) {
            ProductResponse product = productServiceClient.getProductDetails(cartItem.getProductId());
            if(product==null || product.getStock() < cartItem.getQuantity()) {
                assert product != null;
                throw new RuntimeException(product.getName()+" is out od stock.");
            }
            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setStatus("CREATED");
        order = orderRepository.save(order);

        List<OrderItem> orderItemList = new ArrayList<>();
        for(CartItem cartItem: allCartItems) {
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId().toString());
            item.setQuantity(cartItem.getQuantity());
            item.setProductId(cartItem.getProductId());
            item.setPrice(cartItem.getPrice());
            orderItemList.add(item);
            ReduceProductRequest reduceProductRequest = new ReduceProductRequest();
            reduceProductRequest.setQuantity(cartItem.getQuantity());
            reduceProductRequest.setProductId(cartItem.getProductId());
            Boolean isSuccess = productServiceClient.reduceProductStock(reduceProductRequest);
        }

        orderItemRepository.saveAll(orderItemList);
        return true;
    }
}
