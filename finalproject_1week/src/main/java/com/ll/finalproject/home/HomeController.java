package com.ll.finalproject.home;

import com.ll.finalproject.post.controller.PostController;
import com.ll.finalproject.post.dto.PostDto;
import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.post.service.PostService;
import com.ll.finalproject.posthashtag.entity.PostHashTag;
import com.ll.finalproject.posthashtag.service.PostHashTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;
    private final PostHashTagService postHashTagService;

    @RequestMapping("/")
    public String showHome(Model model) {
        List<PostDto> postDtoList = new ArrayList<>();
        List<Post> postList = postService.findTop100ByOrderByIdDesc();
        for (Post post : postList) {
            List<PostHashTag> postHashTagList = postHashTagService.findAllByPost(post);
            PostDto postDto = new PostDto(post, postHashTagList);
            postDtoList.add(postDto);
        }

        model.addAttribute("postDtoList", postDtoList);

        return "home/main";
    }
}
