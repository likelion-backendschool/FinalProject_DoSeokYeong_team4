package com.ll.finalproject.post.repository;

import com.ll.finalproject.member.entity.Member;
import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.postkeyword.entity.PostKeyword;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findAllPostByMemberIdAndPostKeyword(Long memberId, String keyword);
}
