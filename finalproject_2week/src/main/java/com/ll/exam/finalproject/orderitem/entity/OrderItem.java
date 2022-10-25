package com.ll.exam.finalproject.orderitem.entity;

import com.ll.exam.finalproject.app.base.entity.BaseEntity;
import com.ll.exam.finalproject.app.product.entity.Product;
import com.ll.exam.finalproject.order.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString(callSuper = true)
@NoArgsConstructor(access = PROTECTED)
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Order order;

    @ManyToOne(fetch = LAZY)
    private Product product;

    private LocalDateTime payDate; // 결제 날짜
    private int price;  // 가격
    private int salePrice; // 실제 판매가
    private int wholesalePrice; // 도매가
    private int pgFee; // 결제대행사 수수료
    private int payPrice; // 결제금액
    private int refundPrice; // 환불금액
    private boolean isPaid; // 결제 여부

    public OrderItem(Product productOption) {
        this.product = productOption;
        this.price = productOption.getPrice();
        this.salePrice = productOption.getSalePrice();
        this.wholesalePrice = productOption.getWholesalePrice();
    }

    public int calculatePayPrice() {
        return salePrice;
    }

    public void setPaymentDone() {
        this.pgFee = 0;
        this.payPrice = calculatePayPrice();
        this.isPaid = true;
        this.payDate = LocalDateTime.now();
    }

    public void setRefundDone() {
        this.refundPrice = payPrice;
    }

}