package com.ll.exam.finalproject.app.mybook.controller;

import com.ll.exam.finalproject.app.base.dto.RsData;
import com.ll.exam.finalproject.app.base.rq.Rq;
import com.ll.exam.finalproject.app.member.dto.LoginResponse;
import com.ll.exam.finalproject.app.member.dto.MeResponse;
import com.ll.exam.finalproject.app.member.dto.MemberDto;
import com.ll.exam.finalproject.app.mybook.dto.*;
import com.ll.exam.finalproject.app.mybook.entity.MyBook;
import com.ll.exam.finalproject.app.mybook.service.MyBookService;
import com.ll.exam.finalproject.app.security.dto.MemberContext;
import com.ll.exam.finalproject.util.Ut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;

@RestController
@RequestMapping("/api/v1/myBooks")
@RequiredArgsConstructor
@Tag(name = "ApiV1MyBooksController", description = "MyBook을 담당하는 컨트롤러")
public class ApiV1MyBooksController {
    private final MyBookService myBookService;
    private final Rq rq;

    @Operation(summary = "내 도서 리스트 구현", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "", consumes = ALL_VALUE)
    public ResponseEntity<RsData<MyBooksResponse>> myBooks(){

        List<MyBookDto> myBooks = myBookService.findAllByOwnerId(rq.getId())
                .stream()
                .map(MyBookDto::of).toList();

        return Ut.sp.responseEntityOf(
                RsData.successOf(MyBooksResponse.builder().myBooks(myBooks).build())
        );
    }

    @GetMapping(value = "/{myBookId}", consumes = ALL_VALUE)
    @Operation(summary =  "로그인된 회원이 보유한 도서 단건", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<RsData<MyBookResponse>> myBook(@PathVariable long myBookId){

        MyBook myBook = myBookService.findByIdAndMemberId(myBookId, rq.getId());

        List<BookChapterDto> bookChapters = myBookService.getBookChapters(myBook);


        return Ut.sp.responseEntityOf(
                RsData.successOf(
                        MyBookResponse.builder()
                                .myBook(MyBookDetailDto.of(myBook, bookChapters))
                                .build()
                )
        );
    }
}
