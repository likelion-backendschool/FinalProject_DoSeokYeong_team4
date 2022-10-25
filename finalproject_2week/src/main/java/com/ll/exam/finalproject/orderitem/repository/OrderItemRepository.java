package com.ll.exam.finalproject.orderitem.repository;

import com.ll.exam.finalproject.orderitem.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findAllByIdLessThan(Long id, Pageable pageable);

    Page<OrderItem> findAllByIdBetween(Long fromId, Long toId, Pageable pageable);

    Page<OrderItem> findAllByIsPaid(boolean isPaid, Pageable pageable);

    Page<OrderItem> findAllByPayDateBetween(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);
}
