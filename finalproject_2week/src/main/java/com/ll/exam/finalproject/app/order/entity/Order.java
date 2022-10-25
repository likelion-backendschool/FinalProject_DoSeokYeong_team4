package com.ll.exam.finalproject.app.order.entity;

import com.ll.exam.finalproject.app.base.entity.BaseEntity;
import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.orderitem.entity.OrderItem;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class Order extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Member member;

    private LocalDateTime payDate; // 결제 날짜

    private boolean readyStatus; // 주문 완료 여부

    private boolean isPaid; // 결제 완료 여부

    private boolean isCanceled; // 취소 여부

    private boolean isRefunded; // 환불 여부

    private String name; // 주문 명

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);

        orderItems.add(orderItem);
    }

    public int calculatePayPrice() {
        int payPrice = 0;

        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.calculatePayPrice();
        }

        return payPrice;
    }

    public void setPaymentDone(Order order) {
        for (OrderItem orderItem : orderItems) {
            orderItem.setPaymentDone();
        }
    }
}
