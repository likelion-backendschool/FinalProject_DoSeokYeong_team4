package com.ll.finalproject.base;

import com.ll.finalproject.member.Entity.Member;
import com.ll.finalproject.member.service.MemberService;
import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.post.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DevInitData {

    @Bean
    public CommandLineRunner initData(MemberService memberService, PostService postService) {

        return args -> {
            Member member1 = memberService.join("user1", "1234", "dho88dho@naver.com");
            Member member2 = memberService.join("user2", "1234", "test2@test.com");
            Member member3 = memberService.join("user3", "1234", "test3@test.com");

            Post post1 = postService.createPost("subject1", "content1", "contentHtml1", member1);
            Post post2 = postService.createPost("subject2", "content2", "contentHtml2", member2);
            Post post3 = postService.createPost("subject3", "content3", "contentHtml3", member3);
        };
    }

}
