package com.ll.exam.finalproject.app.order.entity;

import com.ll.exam.finalproject.app.base.entity.BaseEntity;
import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.orderitem.entity.OrderItem;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Table(name = "product_order")
public class Order extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private Member member;

    private LocalDateTime refundDate;
    private LocalDateTime payDate;
    private LocalDateTime cancelDate;
    private boolean isPaid; // 결제 완료 여부
    private boolean isCanceled; // 취소 여부
    private boolean isRefunded; // 환불 여부

    private String name; // 주문 명

    @Builder.Default
    @ToString.Exclude
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

    public void setCancelDone() {
        cancelDate = LocalDateTime.now();

        isCanceled = true;
    }

    public void setPaymentDone() {
        payDate = LocalDateTime.now();

        for (OrderItem orderItem : orderItems) {
            orderItem.setPaymentDone();
        }

        isPaid = true;
    }

    public void setRefundDone() {
        for (OrderItem orderItem : orderItems) {
            orderItem.setRefundDone();
        }
    }

    public int getPayPrice() {
        int payPrice = 0;
        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.getPayPrice();
        }

        return payPrice;
    }

    public boolean isPayable() {
        if ( isPaid ) return false;
        if ( isCanceled ) return false;

        return true;
    }

    public void makeName() {
        String name = orderItems.get(0).getProduct().getSubject();

        if (orderItems.size() > 1) {
            name += " 외 %d권".formatted(orderItems.size() - 1);
        }

        this.name = name;
    }
}
