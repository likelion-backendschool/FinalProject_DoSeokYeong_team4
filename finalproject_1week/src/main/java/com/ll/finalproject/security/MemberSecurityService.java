package com.ll.finalproject.security;

import com.ll.finalproject.member.entity.Member;
import com.ll.finalproject.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElse(null);

        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (member.getAuthLevel() == 7) {
            authorities.add(new SimpleGrantedAuthority("Writer"));
        }
        authorities.add(new SimpleGrantedAuthority("User"));
        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}
