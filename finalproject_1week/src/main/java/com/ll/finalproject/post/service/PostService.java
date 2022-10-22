package com.ll.finalproject.post.service;

import com.ll.finalproject.member.entity.Member;
import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.post.repository.PostRepository;
import com.ll.finalproject.posthashtag.repository.PostHashTagRepository;
import com.ll.finalproject.postkeyword.entity.PostKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostHashTagRepository postHashTagRepository;

    public List<Post> findAllPost() {
        List<Post> postList = postRepository.findAll();

        return postList;
    }

    public List<Post> findTop100ByOrderByIdDesc() {
        List<Post> postList = postRepository.findTop100ByOrderByIdDesc();

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

    public Post findById(Long id) {
        Post post = postRepository.findById(id).orElse(null);

        return post;
    }

    public Post modifyPost(Post post, String subject, String content, String contentHtml) {
        post.setSubject(subject);
        post.setContent(content);
        post.setContentHtml(contentHtml);

        postRepository.save(post);

        return post;
    }

    public void deletePost(Post post) {
        postRepository.delete(post);
    }

    public List<Post> findAllPostByMemberIdAndPostKeyword(Long id, String content) {
        return postRepository.findAllPostByMemberIdAndPostKeyword(id, content);
    }
}
