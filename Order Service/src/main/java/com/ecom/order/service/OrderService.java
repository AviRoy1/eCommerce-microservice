package com.ecom.order.service;

import com.ecom.order.clients.ProductServiceClient;
import com.ecom.order.clients.UserServiceClient;
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
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Transactional
    public @Nullable Boolean createOrder(String userId) {
        UserResponse user = userServiceClient.findUserById(userId);
        if(user == null){
            throw new RuntimeException("User not found!!");
        }

        List<CartItem> allCartItems = cartItemRepository.findByUserIdAndActive(userId);
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
        order.setStatus("CONFIRMED");
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
        this.clearCart(allCartItems);
        orderItemRepository.saveAll(orderItemList);

        rabbitTemplate.convertAndSend(exchangeName, routingKey,
                Map.of("orderId", order.getId(), "status", "CREATED"));

        return true;
    }

    private void clearCart(List<CartItem> cartItems) {
        for(CartItem cartItem: cartItems) {
            cartItem.setIsRemoved(true);
        }
        cartItemRepository.saveAll(cartItems);
    }

}
