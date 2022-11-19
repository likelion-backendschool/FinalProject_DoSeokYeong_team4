package com.ll.exam.finalproject.app.mybook.repository;

import com.ll.exam.finalproject.app.mybook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {

}
