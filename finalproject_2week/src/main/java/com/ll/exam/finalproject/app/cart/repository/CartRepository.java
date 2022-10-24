package com.ll.exam.finalproject.app.cart.repository;

import com.ll.exam.finalproject.app.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByMemberIdAndProductId(Long memberId, Long productId);

    List<CartItem> findByMemberId(Long memberId);
}
