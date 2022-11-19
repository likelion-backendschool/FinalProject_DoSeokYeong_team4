package com.ll.exam.finalproject.app.email.repository;

import com.ll.exam.finalproject.app.email.entity.SendEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SendEmailLogRepository extends JpaRepository<SendEmailLog, Long> {
}
