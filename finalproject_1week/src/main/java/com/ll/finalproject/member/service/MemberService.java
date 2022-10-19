package com.ll.finalproject.member.service;

import com.ll.finalproject.member.entity.Member;
import com.ll.finalproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member join(String username, String password, String nickname, String email) {
        if (nickname == null) {

        }
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .email(email)
                .authLevel(7)
                .build();

        memberRepository.save(member);

        return member;
    }

    public Member join(String username, String password, String email) {
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .authLevel(3)
                .build();

        memberRepository.save(member);

        return member;
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElse(null);
    }

    public void modify(Member member, String email) {
        member.setEmail(email);
        memberRepository.save(member);
    }

    public void modify(Member member, String email, String nickname) {
        member.setEmail(email);
        member.setNickname(nickname);
        member.setAuthLevel(7);

        memberRepository.save(member);
    }

    public void modifyPassword(Member member, String newPassword) {
        member.setPassword(passwordEncoder.encode(newPassword));

        memberRepository.save(member);
    }

    public Member findByEmail(String email) {
       return memberRepository.findByEmail(email).orElse(null);
    }

    public Member findByUsernameAndEmail(String username, String email) {
        return memberRepository.findByUsernameAndEmail(username, email).orElse(null);
    }
}
