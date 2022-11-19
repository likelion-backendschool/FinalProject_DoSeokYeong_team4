package com.ll.exam.finalproject.app.mybook.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MyBooksResponse {
    private List<MyBookDto> myBooks;
}