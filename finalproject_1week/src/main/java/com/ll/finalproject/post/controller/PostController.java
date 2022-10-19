package com.ll.finalproject.post.controller;

import com.ll.finalproject.member.Entity.Member;
import com.ll.finalproject.member.service.MemberService;
import com.ll.finalproject.post.PostCreateForm;
import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.post.service.PostService;
import com.ll.finalproject.posthashtag.entity.PostHashTag;
import com.ll.finalproject.posthashtag.service.PostHashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;
    private final PostHashTagService postHashTagService;

    @GetMapping("/list")
    public String showList(Model model) {
        List<Post> postList = postService.findAllPost();

        model.addAttribute("postList", postList);

        return "post/list";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable("id") Long id, Model model) {
        Post post = postService.findById(id);
        List<PostHashTag> postHashTagList = postHashTagService.findAllByPost(post);

        String postHashTags = "";
        for (PostHashTag postHashTag : postHashTagList) {
            postHashTags += "#" + postHashTag.getPostKeyword().getContent() + " ";
        }

        model.addAttribute("post", post);
        model.addAttribute("postHashTags", postHashTags);

        return "post/detail";
    }

    @GetMapping("/write")
    public String showWrite() {

        return "post/write";
    }

    @PostMapping("/write")
    @PreAuthorize("isAuthenticated()")
    public String doWrite(@Valid PostCreateForm postCreateForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "post/write";
        }
        // 작성자
        Member member = memberService.findByUsername(principal.getName());

        // 해시태그 목록
        String keywords = postCreateForm.getKeywords();

        Post post = postService.createPost(postCreateForm.getSubject(), postCreateForm.getContent(),
                postCreateForm.getContentHtml(), member);

        HashSet<String> keywordSet = Arrays.stream(keywords.split("#"))
                .parallel().filter(s -> s.trim().length() > 0)
                .map(String::trim)
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

        postHashTagService.setPostHashTag(keywordSet, member, post);

        return String.format("redirect:/post/%d".formatted(post.getId()));
    }

    @GetMapping("/{id}/modify")
    @PreAuthorize("isAuthenticated()")
    public String showModify(@PathVariable("id") Long id, Model model) {
        Post post = postService.findById(id);
        List<PostHashTag> postHashTagList = postHashTagService.findAllByPost(post);
        String postHashTags = "";
        for (PostHashTag postHashTag : postHashTagList) {
            postHashTags += "#" + postHashTag.getPostKeyword().getContent() + " ";
        }

        model.addAttribute("post", post);
        model.addAttribute("postHashTags", postHashTags);

        return "post/modify";
    }

    @PostMapping("/{id}/modify")
    @PreAuthorize("isAuthenticated()")
    public String doModify(@Valid PostCreateForm postCreateForm, BindingResult bindingResult, Principal principal,
                           @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "post/modify";
        }
        // 로그인 유저
        Member member = memberService.findByUsername(principal.getName());

        // 포스트
        Post post = postService.findById(id);

        // 작성자 아닐경우
        if (post.getAuthorId() != member) {
            return "post/modify";
        }

        // 수정
        postService.modifyPost(post, postCreateForm.getSubject(), postCreateForm.getContent(),
                postCreateForm.getContentHtml());

        // 해시태그 목록
        String keywords = postCreateForm.getKeywords();

        HashSet<String> keywordSet = Arrays.stream(keywords.split("#"))
                .parallel().filter(s -> s.trim().length() > 0)
                .map(String::trim)
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

        postHashTagService.setPostHashTag(keywordSet, member, post);

        return String.format("redirect:/post/%d".formatted(post.getId()));
    }

    @GetMapping("/{id}/delete")
    @PreAuthorize("isAuthenticated()")
    public String deletePost(@PathVariable("id") Long id, Principal principal) {
        // 로그인 유저
        Member member = memberService.findByUsername(principal.getName());

        // 포스트
        Post post = postService.findById(id);

        // 작성자 아닐경우
        if (post.getAuthorId() != member) {
            return String.format("redirect:/post/%d?msg=%s".formatted(post.getId(), "작성자가 아닙니다."));
        }

        postService.deletePost(post);

        return String.format("redirect:/post/list");
    }
}
