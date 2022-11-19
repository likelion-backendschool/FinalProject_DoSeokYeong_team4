package com.ll.exam.finalproject.app.mybook.repository;

import com.ll.exam.finalproject.app.mybook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    void deleteByProductIdAndMemberId(long productId, long ownerId);

    List<MyBook> findAllByMemberId(long memberId);
    Optional<MyBook> findByIdAndMemberId(long myBookId, long memberId);
}
