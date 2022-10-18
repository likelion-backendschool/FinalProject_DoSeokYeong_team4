package com.ll.finalproject.base;

import com.ll.finalproject.member.Entity.Member;
import com.ll.finalproject.member.service.MemberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DevInitData {

    @Bean
    public CommandLineRunner initData(MemberService memberService) {

        return args -> {
            Member member1 = memberService.join("user1", "1234", "dho88dho@naver.com");
            Member member2 = memberService.join("user2", "1234", "test2@test.com");
            Member member3 = memberService.join("user3", "1234", "test3@test.com");
        };
    }

}
