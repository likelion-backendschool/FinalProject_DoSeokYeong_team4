package com.ll.finalproject.post.service;

import com.ll.finalproject.member.Entity.Member;
import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<Post> findAllPost() {
        List<Post> postList = postRepository.findAll();

        return postList;
    }

    public Post createPost(String subject, String content, String contentHtml, Member author) {
        Post post = Post.builder()
                .subject(subject)
                .content(content)
                .contentHtml(contentHtml)
                .authorId(author)
                .build();

        postRepository.save(post);

        return post;
    }
}
