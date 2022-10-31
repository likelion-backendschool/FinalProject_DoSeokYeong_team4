package com.ll.exam.finalproject.app.rebate.repository;

import com.ll.exam.finalproject.app.rebate.entity.RebateOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RebateRepository extends JpaRepository<RebateOrderItem, Long> {

    Optional<RebateOrderItem> findByOrderItemId(long orderItemId);
}
