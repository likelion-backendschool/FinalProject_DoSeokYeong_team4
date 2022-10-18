package com.ll.finalproject.product.Entity;

import com.ll.finalproject.base.BaseEntity;
import com.ll.finalproject.member.Entity.Member;
import com.ll.finalproject.post.Entity.PostKeyword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class Product extends BaseEntity {

    private String subject; // 이름

    private int price; // 가격

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member authorId; // 회원번호, FK

    @OneToOne
    @JoinColumn(name = "postKeyword_id")
    private PostKeyword postKeywordId; // 글 키워드 번호, FK
}
