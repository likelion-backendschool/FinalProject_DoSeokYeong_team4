package com.ll.exam.finalproject.order.service;

import com.ll.exam.finalproject.app.cart.entity.CartItem;
import com.ll.exam.finalproject.app.cart.service.CartService;
import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.member.service.MemberService;
import com.ll.exam.finalproject.app.product.entity.Product;
import com.ll.exam.finalproject.order.entity.Order;
import com.ll.exam.finalproject.order.repository.OrderRepository;
import com.ll.exam.finalproject.orderitem.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final MemberService memberService;
    private final CartService cartService;
    private final OrderRepository orderRepository;

    @Transactional
    public Order createFromCart(Member member) {
        List<CartItem> cartItems = cartService.findByMemberId(member.getId());

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product productOption = cartItem.getProduct();

            if (productOption.isOrderable()) {
                orderItems.add(new OrderItem(productOption));

            }

            cartService.deleteItem(cartItem);
        }

        return create(member, orderItems);
    }

    @Transactional
    public Order create(Member member, List<OrderItem> orderItems) {
        Order order = Order.builder()
                .member(member)
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        orderRepository.save(order);

        return order;
    }

    public void payByRestCashOnly(Order order) {
        Member member = order.getMember();
        long restCash = member.getRestCash();

        int payPrice = order.calculatePayPrice();

        if (restCash < payPrice) {
            throw new RuntimeException("잔액 부족");
        }

        memberService.addCash(member, payPrice * -1, "주문결제__예치금결제");

        order.setPaymentDone(order);
        orderRepository.save(order);
    }

}
