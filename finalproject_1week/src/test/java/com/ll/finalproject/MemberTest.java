package com.ll.finalproject;

import com.ll.finalproject.member.entity.Member;
import com.ll.finalproject.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    MemberService memberService;

    @Transactional
    @DisplayName("회원가입테스트")
    @Test
    void t1() {
        Member member = memberService.join("user1", "1234", null, "email");

        System.out.println(member.toString());
    }

    @DisplayName("UUID테스트")
    @Test
    void t2() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        System.out.println(uuid);
    }

}
