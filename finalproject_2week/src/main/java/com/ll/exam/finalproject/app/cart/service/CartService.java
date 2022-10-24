package com.ll.exam.finalproject.app.cart.service;

import com.ll.exam.finalproject.app.cart.entity.CartItem;
import com.ll.exam.finalproject.app.cart.repository.CartRepository;
import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.product.entity.Product;
import com.ll.exam.finalproject.orderitem.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public CartItem addItem(Member member, Product productOption) {
        CartItem oldCartItem = cartRepository.findByMemberIdAndProductId(member.getId(), productOption.getId()).orElse(null);

        if ( oldCartItem != null ) {
            cartRepository.save(oldCartItem);

            return oldCartItem;
        }

        CartItem cartItem = CartItem.builder()
                .member(member)
                .product(productOption)
                .build();

        cartRepository.save(cartItem);

        return cartItem;
    }

    public void deleteItem(CartItem cartItem) {
        cartRepository.delete(cartItem);
    }

    public Optional<CartItem> findById(long productId) {
        return cartRepository.findById(productId);
    }

    public List<CartItem> findByMemberId(Long memberId) {
        return cartRepository.findByMemberId(memberId);
    }
}
