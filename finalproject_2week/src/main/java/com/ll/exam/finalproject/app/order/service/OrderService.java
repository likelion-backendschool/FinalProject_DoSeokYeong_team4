package com.ll.exam.finalproject.app.order.service;

import com.ll.exam.finalproject.app.base.dto.RsData;
import com.ll.exam.finalproject.app.cart.entity.CartItem;
import com.ll.exam.finalproject.app.cart.service.CartService;
import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.member.service.MemberService;
import com.ll.exam.finalproject.app.mybook.service.MyBookService;
import com.ll.exam.finalproject.app.order.entity.Order;
import com.ll.exam.finalproject.app.order.repository.OrderRepository;
import com.ll.exam.finalproject.app.orderitem.entity.OrderItem;
import com.ll.exam.finalproject.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final MemberService memberService;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final MyBookService myBookService;

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

        // 주문 품목으로 부터 이름을 만든다.
        order.makeName();

        orderRepository.save(order);

        return order;
    }

    @Transactional
    public RsData payByRestCashOnly(Order order) {
        Member member = order.getMember();
        long restCash = memberService.getRestCash(member);

        int payPrice = order.calculatePayPrice();

        if (restCash < payPrice) {
            throw new RuntimeException("잔액 부족");
        }

        memberService.addCash(member, payPrice * -1, "주문__%d__사용__예치금".formatted(order.getId()));

        order.setPaymentDone();

        myBookService.createMyBook(order);

        orderRepository.save(order);

        return RsData.of("S-1", "결제가 완료되었습니다.");
    }

    @Transactional
    public RsData refund(Order order, Member actor) {
        RsData actorCanRefundRsData = actorCanRefund(actor, order);

        if (actorCanRefundRsData.isFail()) {
            return actorCanRefundRsData;
        }

        order.setCancelDone();

        int payPrice = order.getPayPrice();
        memberService.addCash(order.getMember(), payPrice, "주문__%d__환불__예치금".formatted(order.getId()));

        order.setRefundDone();
        orderRepository.save(order);

        return RsData.of("S-1", "환불되었습니다.");
    }

    @Transactional
    public RsData refund(Long orderId, Member actor) {
        Order order = findById(orderId);

        if (order == null) {
            return RsData.of("F-2", "결제 상품을 찾을 수 없습니다.");
        }
        return refund(order, actor);
    }

    public List<Order> findAllByMemberId(long memberId) {
        return orderRepository.findAllByMemberIdOrderByIdDesc(memberId);
    }

    public Order findById(long id) {
        return orderRepository.findById(id).orElse(null);
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
    public RsData payByTossPayments(Order order, long useRestCash) {
        Member buyer = order.getMember();
        int payPrice = order.calculatePayPrice();

        long pgPayPrice = payPrice - useRestCash;
        memberService.addCash(buyer, pgPayPrice, "주문__%d__충전__토스페이먼츠".formatted(order.getId()));
        memberService.addCash(buyer, pgPayPrice * -1, "주문__%d__사용__토스페이먼츠".formatted(order.getId()));

        if (useRestCash > 0) {
            memberService.addCash(buyer, useRestCash * -1, "주문__%d__사용__예치금".formatted(order.getId()));
        }

        order.setPaymentDone();
        orderRepository.save(order);
        myBookService.createMyBook(order);

        return RsData.of("S-1", "결제가 완료되었습니다.");
    }

    @Transactional
    public RsData cancel(Order order, Member actor) {
        RsData actorCanCancelRsData = actorCanCancel(actor, order);

        if (actorCanCancelRsData.isFail()) {
            return actorCanCancelRsData;
        }

        order.setCanceled(true);

        return RsData.of("S-1", "취소되었습니다.");
    }

    @Transactional
    public RsData cancel(Long orderId, Member actor) {
        Order order = findById(orderId);
        return cancel(order, actor);
    }

    public RsData actorCanCancel(Member actor, Order order) {
        if ( order.isPaid() ) {
            return RsData.of("F-3", "이미 결제처리 되었습니다.");
        }

        if (order.isCanceled()) {
            return RsData.of("F-1", "이미 취소되었습니다.");
        }

        if (actor.getId().equals(order.getMember().getId()) == false) {
            return RsData.of("F-2", "권한이 없습니다.");
        }

        return RsData.of("S-1", "취소할 수 있습니다.");
    }

    public RsData actorCanRefund(Member actor, Order order) {

        if (order.isCanceled()) {
            return RsData.of("F-1", "이미 취소되었습니다.");
        }

        if ( order.isRefunded() ) {
            return RsData.of("F-4", "이미 환불되었습니다.");
        }

        if ( order.isPaid() == false ) {
            return RsData.of("F-5", "결제가 되어야 환불이 가능합니다.");
        }

        if (actor.getId().equals(order.getMember().getId()) == false) {
            return RsData.of("F-2", "권한이 없습니다.");
        }

        long between = ChronoUnit.MINUTES.between(order.getPayDate(), LocalDateTime.now());

        if (between > 10) {
            return RsData.of("F-3", "결제 된지 10분이 지났으므로, 환불 할 수 없습니다.");
        }

        return RsData.of("S-1", "환불할 수 있습니다.");
    }
}
