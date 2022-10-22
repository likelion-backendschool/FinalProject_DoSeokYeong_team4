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

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostHashTagService {
    private final PostKeywordService postKeywordService;
    private final PostKeywordRepository postKeywordRepository;
    private final PostHashTagRepository postHashTagRepository;

    public void setPostHashTag(String keywords, Member member, Post post) {

        HashSet<String> keywordSet = Arrays.stream(keywords.split("#"))
                .parallel().filter(s -> s.trim().length() > 0)
                .map(String::trim)
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

        List<PostHashTag> oldHashTags = postHashTagRepository.findAllByPostId(post); // 기존 해시 태그들

        List<PostHashTag> needToDelete = new ArrayList<>();

        for (PostHashTag hashTag : oldHashTags) { // 기존 해시태그에서

            boolean contains = keywordSet.stream().anyMatch(s -> s.equals(hashTag.getPostKeyword().getContent()));

            if (contains == false) {
                needToDelete.add(hashTag);
            }
        }

        needToDelete.forEach(hashTag -> {
            postHashTagRepository.delete(hashTag);
        });

        keywordSet.forEach(keywordContent -> {
            saveHashTag(post, keywordContent, member);
        });

    }

    private PostHashTag saveHashTag(Post post, String keywordContent, Member member) {
        PostKeyword postKeyword = postKeywordRepository.findByContent(keywordContent).orElse(null);

        if (postKeyword == null) { // 없으면 만든다.
            postKeyword = postKeywordService.createPostKeywordService(keywordContent);
        }

        PostHashTag existPostHashTag = postHashTagRepository.findByPostIdAndPostKeyword(post, postKeyword);

        if (existPostHashTag != null) {
            return existPostHashTag;
        }

        PostHashTag postHashTag = PostHashTag.builder()
                .postId(post)
                .memberId(member)
                .postKeyword(postKeyword)
                .build();

        postHashTagRepository.save(postHashTag);

        return postHashTag;
    }

    public List<PostHashTag> findAllByPost(Post post) {
        List<PostHashTag> postHashTagList = postHashTagRepository.findAllByPostId(post);

        return postHashTagList;
    }
}
