package com.ll.finalproject.posthashtag.service;

import com.ll.finalproject.member.entity.Member;
import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.posthashtag.entity.PostHashTag;
import com.ll.finalproject.posthashtag.repository.PostHashTagRepository;
import com.ll.finalproject.postkeyword.entity.PostKeyword;
import com.ll.finalproject.postkeyword.repository.PostKeywordRepository;
import com.ll.finalproject.postkeyword.service.PostKeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostHashTagService {
    private final PostKeywordService postKeywordService;
    private final PostKeywordRepository postKeywordRepository;
    private final PostHashTagRepository postHashTagRepository;

    public void setPostHashTag(HashSet<String> keywordSet, Member member, Post post) {

        List<PostHashTag> oldHashTags = postHashTagRepository.findAllByPostId(post); // 기존 해시 태그들

        List<PostHashTag> needToDelete = new ArrayList<>();

        for (PostHashTag hashTag : oldHashTags) {

            boolean contains = keywordSet.stream().anyMatch(s -> s.equals(hashTag.getPostKeyword().getContent()));

            if (contains == false) {
                needToDelete.add(hashTag);
            }
        }

        keywordSet.forEach(keywordContent -> {
            saveHashTag(post, keywordContent, member);
        });

        needToDelete.forEach(hashTag -> {
            postHashTagRepository.delete(hashTag);
        });

//        for (String keyword : keywordSet) {
//            PostKeyword postKeyword = postKeywordRepository.findByContent(keyword).orElse(null);
//            if (postKeyword == null) { // 없으면 만든다.
//                postKeyword = postKeywordService.createPostKeywordService(keyword);
//            }
//            PostHashTag postHashTag = PostHashTag.builder()
//                    .postId(post)
//                    .memberId(member)
//                    .postKeyword(postKeyword)
//                    .build();
//
//            postHashTagRepository.save(postHashTag);
//        }
    }

    private void saveHashTag(Post post, String keywordContent, Member member) {
        PostKeyword postKeyword = postKeywordRepository.findByContent(keywordContent).orElse(null);

        if (postKeyword == null) { // 없으면 만든다.
            postKeyword = postKeywordService.createPostKeywordService(keywordContent);
        }
        PostHashTag postHashTag = PostHashTag.builder()
                .postId(post)
                .memberId(member)
                .postKeyword(postKeyword)
                .build();

        postHashTagRepository.save(postHashTag);
    }

    public List<PostHashTag> findAllByPost(Post post) {
        List<PostHashTag> postHashTagList = postHashTagRepository.findAllByPostId(post);

        return postHashTagList;
    }
}
