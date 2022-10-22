package com.ll.finalproject.post.entity;

import com.ll.finalproject.base.BaseEntity;
import com.ll.finalproject.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class Post extends BaseEntity {
    private String subject;

    private String content; // 내용(마크다운)

    private String contentHtml; // 내용(토스트에디터의 렌더링 결과, HTML)

    @ManyToOne(fetch = FetchType.LAZY)
    private Member authorId;
}
