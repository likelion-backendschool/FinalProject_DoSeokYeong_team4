package com.ll.finalproject.posthashtag.repository;

import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.posthashtag.entity.PostHashTag;
import com.ll.finalproject.postkeyword.entity.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {
    List<PostHashTag> findAllByPostId(Post post);

    PostHashTag findByPostIdAndPostKeyword(Post post, PostKeyword postKeyword);
}
