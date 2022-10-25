package com.ll.exam.finalproject.app.order.repository;

import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByMemberId(Member member);
}
