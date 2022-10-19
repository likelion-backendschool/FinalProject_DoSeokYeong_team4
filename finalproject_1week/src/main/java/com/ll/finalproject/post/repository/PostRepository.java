package com.ll.finalproject.post.repository;

import com.ll.finalproject.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    List<Post> findTop100ByOrderByIdDesc();
}
