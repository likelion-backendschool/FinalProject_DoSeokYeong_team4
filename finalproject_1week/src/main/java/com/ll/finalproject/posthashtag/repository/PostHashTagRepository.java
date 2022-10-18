package com.ll.finalproject.posthashtag.repository;

import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.posthashtag.entity.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {
    List<PostHashTag> findAllByPostId(Post post);
}
