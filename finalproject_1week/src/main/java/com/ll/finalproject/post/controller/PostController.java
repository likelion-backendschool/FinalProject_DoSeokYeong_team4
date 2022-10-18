package com.ll.finalproject.post.controller;

import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @GetMapping("/list")
    public String showList(Model model) {
        List<Post> postList = postService.findAllPost();

        model.addAttribute("postList", postList);

        return "post/list";
    }
}
