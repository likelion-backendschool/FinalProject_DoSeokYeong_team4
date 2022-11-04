package com.ll.exam.finalproject.app.mybook.service;

import com.ll.exam.finalproject.app.base.rq.Rq;
import com.ll.exam.finalproject.app.mybook.entity.MyBook;
import com.ll.exam.finalproject.app.mybook.repository.MyBookRepository;
import com.ll.exam.finalproject.app.order.entity.Order;
import com.ll.exam.finalproject.app.orderitem.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyBookService {
    private final Rq rq;
    private final MyBookRepository myBookRepository;

    @Transactional
    public void createMyBook(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            MyBook myBook = MyBook.builder()
                    .member(order.getMember())
                    .product(orderItem.getProduct())
                    .build();

            myBookRepository.save(myBook);
        }
    }
}
