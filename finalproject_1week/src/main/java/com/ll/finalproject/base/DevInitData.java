package com.ll.finalproject.base;

import com.ll.finalproject.member.entity.Member;
import com.ll.finalproject.member.service.MemberService;
import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.post.service.PostService;
import com.ll.finalproject.posthashtag.service.PostHashTagService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

@Configuration
public class DevInitData {

    @Bean
    public CommandLineRunner initData(MemberService memberService, PostService postService, PostHashTagService postHashTagService) {

        return args -> {
            Member member1 = memberService.join("user1", "1234", "dho88dho@naver.com");
            Member member2 = memberService.join("user2", "1234", "test2@test.com");
            Member member3 = memberService.join("user3", "1234", "test3@test.com");

            Post post1 = postService.createPost("subject1", "content1", "contentHtml1", member1);
            Post post2 = postService.createPost("subject2", "content2", "contentHtml2", member2);
            Post post3 = postService.createPost("subject3", "content3", "contentHtml3", member3);
            Post post4 = postService.createPost("subject4", "1111111111\n" +
                    " # 2222222222222222\n" +
                    " **33333333333333**\n" +
                    " *444444444444444*\n" +
                    " \n" +
                    " ***\n" +
                    " 5555555555555555\n" +
                    " > 666666666666666666", "<p>1111111111</p><h1>2222222222222222</h1><p><strong>33333333333333</strong></p><p><em>444444444444444</em></p><div contenteditable=\"false\"><hr></div><p>5555555555555555</p><blockquote><p>666666666666666666</p></blockquote>", member1);
            String keywords = "#자바 #스프링부트 #스프링배치";

            HashSet<String> keywordSet = Arrays.stream(keywords.split("#"))
                    .parallel().filter(s -> s.trim().length() > 0)
                    .map(String::trim)
                    .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

            postHashTagService.setPostHashTag(keywordSet, member1, post4);

            String keywords2 = "#자바 #스프링부트 #테스트 #확인";

            HashSet<String> keywordSet2 = Arrays.stream(keywords2.split("#"))
                    .parallel().filter(s -> s.trim().length() > 0)
                    .map(String::trim)
                    .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

            postHashTagService.setPostHashTag(keywordSet2, member1, post1);

        };
    }

}
