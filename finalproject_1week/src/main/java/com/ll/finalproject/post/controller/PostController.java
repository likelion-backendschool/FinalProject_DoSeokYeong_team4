package com.ll.finalproject.post.controller;

import com.ll.finalproject.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
}
