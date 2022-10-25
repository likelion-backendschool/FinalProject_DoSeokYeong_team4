package com.ll.exam.finalproject.order.repository;

import com.ll.exam.finalproject.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
