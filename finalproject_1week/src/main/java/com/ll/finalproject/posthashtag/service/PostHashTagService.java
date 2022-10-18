package com.ll.finalproject.posthashtag.service;

import com.ll.finalproject.member.Entity.Member;
import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.posthashtag.entity.PostHashTag;
import com.ll.finalproject.posthashtag.repository.PostHashTagRepository;
import com.ll.finalproject.postkeyword.entity.PostKeyword;
import com.ll.finalproject.postkeyword.repository.PostKeywordRepository;
import com.ll.finalproject.postkeyword.service.PostKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostHashTagService {
    private final PostKeywordService postKeywordService;
    private final PostKeywordRepository postKeywordRepository;
    private final PostHashTagRepository postHashTagRepository;

    public void addPostHashTag(HashSet<String> keywordSet, Member member, Post post) {
        for (String keyword : keywordSet) {
            PostKeyword postKeyword = postKeywordRepository.findByContent(keyword).orElse(null);
            if (postKeyword == null) { // 없으면 만든다.
                postKeyword = postKeywordService.createPostKeywordService(keyword);
            }
            PostHashTag postHashTag = PostHashTag.builder()
                    .postId(post)
                    .memberId(member)
                    .postKeyword(postKeyword)
                    .build();

            postHashTagRepository.save(postHashTag);
        }
    }

    public List<PostHashTag> findAllByPost(Post post) {
        List<PostHashTag> postHashTagList = postHashTagRepository.findAllByPostId(post);

        return postHashTagList;
    }
}
