package com.ll.exam.finalproject.app.order.service;

import com.ll.exam.finalproject.app.cart.entity.CartItem;
import com.ll.exam.finalproject.app.cart.service.CartService;
import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.member.service.MemberService;
import com.ll.exam.finalproject.app.order.entity.Order;
import com.ll.exam.finalproject.app.order.repository.OrderRepository;
import com.ll.exam.finalproject.app.orderitem.entity.OrderItem;
import com.ll.exam.finalproject.app.product.entity.Product;
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

    @Transactional
    public void payByRestCashOnly(Order order) {
        Member member = order.getMember();
        long restCash = memberService.getRestCash(member);

        int payPrice = order.calculatePayPrice();

        if (restCash < payPrice) {
            throw new RuntimeException("잔액 부족");
        }

        memberService.addCash(member, payPrice * -1, "주문결제__예치금결제");

        order.setPaymentDone(order);
        orderRepository.save(order);
    }

    public List<Order> findAllByMemberId(long memberId) {
        return orderRepository.findAllByMemberId(memberId);
    }

    public Order findById(long id) {
        return orderRepository.findById(id).get();
    }

    @Transactional
    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    public boolean actorCanModify(Member actor, Order order) {
        if (actor == null) return false;

        return actor.getId().equals(order.getMember().getId());
    }

    public boolean actorCanRemove(Member actor, Order order) {
        if (order.isPaid()) {
            return false;
        }

        return actorCanModify(actor, order);
    }

    public boolean actorCanSee(Member actor, Order order) {
        return actor.getId().equals(order.getMember().getId());
    }

    public boolean actorCanPayment(Member actor, Order order) {
        return actorCanSee(actor, order);
    }

    @Transactional
    public void payByTossPayments(Order order, long useRestCash) {
        Member buyer = order.getMember();
        int payPrice = order.calculatePayPrice();

        long pgPayPrice = payPrice - useRestCash;
        memberService.addCash(buyer, pgPayPrice, "주문__%d__충전__토스페이먼츠".formatted(order.getId()));
        memberService.addCash(buyer, pgPayPrice * -1, "주문__%d__사용__토스페이먼츠".formatted(order.getId()));

        if ( useRestCash > 0 ) {
            memberService.addCash(buyer, useRestCash * -1, "주문__%d__사용__예치금".formatted(order.getId()));
        }

        order.setPaymentDone(order);
        orderRepository.save(order);
    }
}
