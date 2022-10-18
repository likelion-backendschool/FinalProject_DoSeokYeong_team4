package com.ll.finalproject.post.entity;

import com.ll.finalproject.base.BaseEntity;
import com.ll.finalproject.member.Entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class Post extends BaseEntity {
    private String subject;

    private String content; // 내용(마크다운)

    private String contentHtml; // 내용(토스트에디터의 렌더링 결과, HTML)

    @ManyToOne
    private Member authorId;
}
