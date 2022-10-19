package com.ll.finalproject.post.repository;

import com.ll.finalproject.member.entity.Member;
import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.postkeyword.entity.PostKeyword;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.finalproject.post.entity.QPost.post;
import static com.ll.finalproject.member.entity.QMember.member;
import static com.ll.finalproject.posthashtag.entity.QPostHashTag.postHashTag;
import static com.ll.finalproject.postkeyword.entity.QPostKeyword.postKeyword;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findAllPostByMemberIdAndPostKeyword(Long memberId, String keyword) {
        return jpaQueryFactory
                .selectFrom(post)
                .innerJoin(member)
                .on(post.authorId.id.eq(member.id))
                .innerJoin(postHashTag)
                .on(post.id.eq(postHashTag.postId.id))
                .innerJoin(postKeyword)
                .on(postHashTag.postKeyword.id.eq(postKeyword.id))
                .where(post.authorId.id.eq(memberId)
                        .and(
                                postKeyword.content.eq(keyword)
                        )
                )
                .fetch();
    }
}
