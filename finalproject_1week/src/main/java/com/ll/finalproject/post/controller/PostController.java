package com.ll.finalproject.post.controller;

import com.ll.finalproject.member.Entity.Member;
import com.ll.finalproject.member.MemberCreateForm;
import com.ll.finalproject.member.service.MemberService;
import com.ll.finalproject.post.PostCreateForm;
import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.post.service.PostService;
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
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String showList(Model model) {
        List<Post> postList = postService.findAllPost();

        model.addAttribute("postList", postList);

        return "post/list";
    }

    @GetMapping("/{id}")
    public String showList(@PathVariable("id") Long id, Model model) {
        Post post = postService.findById(id);

        model.addAttribute("post", post);

        return "post/detail";
    }

    @GetMapping("/write")
    public String showWrite() {

        return "post/write";
    }

    @PostMapping("/write")
    @PreAuthorize("isAuthenticated()")
    public String doJoin(@Valid PostCreateForm postCreateForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "post/write";
        }
        Member member = memberService.findByUsername(principal.getName());

        Post post = postService.createPost(postCreateForm.getSubject(), postCreateForm.getContent(),
                postCreateForm.getContentHtml(), member);

        return String.format("redirect:/post/%d".formatted(post.getId()));
    }
}
