package com.ll.exam.finalproject.app.mybook.service;

import com.ll.exam.finalproject.app.base.exception.MyBookNotFoundException;
import com.ll.exam.finalproject.app.base.rq.Rq;
import com.ll.exam.finalproject.app.mybook.dto.BookChapterDto;
import com.ll.exam.finalproject.app.mybook.dto.MyBookDto;
import com.ll.exam.finalproject.app.mybook.entity.MyBook;
import com.ll.exam.finalproject.app.mybook.repository.MyBookRepository;
import com.ll.exam.finalproject.app.order.entity.Order;
import com.ll.exam.finalproject.app.orderitem.entity.OrderItem;
import com.ll.exam.finalproject.app.post.service.PostService;
import com.ll.exam.finalproject.app.postTag.entity.PostTag;
import com.ll.exam.finalproject.app.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyBookService {
    private final Rq rq;
    private final MyBookRepository myBookRepository;
    private final PostService postService;

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

    public List<MyBook> findAllByOwnerId(long ownerId) {
        return myBookRepository.findAllByMemberId(ownerId);
    }

    public MyBook findByIdAndMemberId(long myBookId, long id) {
        return myBookRepository.findByIdAndMemberId(myBookId, id).orElseThrow(MyBookNotFoundException::new);
    }

    public List<BookChapterDto> getBookChapters(MyBook myBook) {
        Product product = myBook.getProduct();

        List<PostTag> postTags = postService.getPostTags(product.getAuthor(), product.getPostKeyword().getContent());

        return postTags
                .stream()
                .map(postTag -> postTag.getPost())
                .map(post -> BookChapterDto.of(post))
                .collect(Collectors.toList());
    }
}
