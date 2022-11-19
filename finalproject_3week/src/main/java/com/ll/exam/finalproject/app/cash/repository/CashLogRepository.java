package com.ll.exam.finalproject.app.cash.repository;

import com.ll.exam.finalproject.app.cash.entity.CashLog;
import com.ll.exam.finalproject.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashLogRepository extends JpaRepository<CashLog, Long> {
    Optional<CashLog> findByMember(Member member);
}
