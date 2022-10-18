package com.ll.finalproject.postkeyword.service;

import com.ll.finalproject.postkeyword.entity.PostKeyword;
import com.ll.finalproject.postkeyword.repository.PostKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostKeywordService {
    private final PostKeywordRepository postKeywordRepository;

    public PostKeyword createPostKeywordService(String keyword) {
        PostKeyword postKeyword = PostKeyword.builder()
                .content(keyword)
                .build();

        postKeywordRepository.save(postKeyword);

        return postKeyword;
    }
}
