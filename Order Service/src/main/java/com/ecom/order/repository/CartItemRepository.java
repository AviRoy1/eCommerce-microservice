package com.ecom.order.repository;

import com.ecom.order.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query(value = "select * from cart_item ci join users u on u.id = ci.user_id join products p on p.id = ci.product_id " +
            "where u.id = :userId and p.id = :productId order by ci.created_on limit 1", nativeQuery = true)
    CartItem findByUserIdAndProductId(Long userId, Long productId);

    List<CartItem> findByUserId(String userId);
}
