package com.ll.exam.app_2022_09_22.app.cart.repository;

import com.ll.exam.app_2022_09_22.app.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByMember_idAndProductOption_id(Long memberId, Long productOptionId);
}