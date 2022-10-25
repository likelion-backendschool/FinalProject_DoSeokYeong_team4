package com.ll.exam.finalproject.app.cart.service;

import com.ll.exam.finalproject.app.cart.entity.CartItem;
import com.ll.exam.finalproject.app.cart.repository.CartRepository;
import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public Map<String, String> addItem(Member member, Product productOption) {
        Map<String, String> result = new HashMap<>();

        CartItem oldCartItem = cartRepository.findByMemberIdAndProductId(member.getId(), productOption.getId()).orElse(null);

        if (oldCartItem != null) {
            cartRepository.save(oldCartItem);

            result.put("result", "이미 장바구니에 존재합니다.");
            return result;
        }

        CartItem cartItem = CartItem.builder()
                .member(member)
                .product(productOption)
                .build();

        cartRepository.save(cartItem);

        result.put("result", "장바구니에 추가됐습니다.");
        return result;
    }

    public void deleteItem(CartItem cartItem) {
        cartRepository.delete(cartItem);
    }

    public Optional<CartItem> findByMemberIdAndProductId(long memberId, long productId) {
        return cartRepository.findByMemberIdAndProductId(memberId, productId);
    }

    public List<CartItem> findByMemberId(Long memberId) {
        return cartRepository.findByMemberId(memberId);
    }
}
