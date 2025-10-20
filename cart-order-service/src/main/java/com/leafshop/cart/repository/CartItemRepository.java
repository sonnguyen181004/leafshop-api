package com.leafshop.cart.repository;

import com.leafshop.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Lấy 1 item trong giỏ theo cart và product
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);

    // Lấy tất cả items trong giỏ theo cart id
    List<CartItem> findByCartId(Long cartId);
}
