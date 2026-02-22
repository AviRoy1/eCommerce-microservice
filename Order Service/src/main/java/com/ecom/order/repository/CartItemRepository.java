package com.ecom.order.repository;

import com.ecom.order.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query(value = "select * from cart_item ci where ci.user_id = :userId and ci.product_id = :productId order by ci.created_at limit 1", nativeQuery = true)
    CartItem findByUserIdAndProductId(String userId, String productId);

    List<CartItem> findByUserId(String userId);
}
