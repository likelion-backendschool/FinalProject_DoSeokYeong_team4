package com.ll.exam.finalproject.app.mybook.dto;

import com.ll.exam.finalproject.app.mybook.entity.MyBook;
import com.ll.exam.finalproject.app.product.dto.ProductDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyBookDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long ownerId;
    private ProductDto product;

    public static MyBookDto of(MyBook myBook) {
        return MyBookDto
                .builder()
                .id(myBook.getId())
                .createDate(myBook.getCreateDate())
                .modifyDate(myBook.getModifyDate())
                .ownerId(myBook.getMember().getId())
                .product(ProductDto.of(myBook.getProduct()))
                .build();
    }
}
