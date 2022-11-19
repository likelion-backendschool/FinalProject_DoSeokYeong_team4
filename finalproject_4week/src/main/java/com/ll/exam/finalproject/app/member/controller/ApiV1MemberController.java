package com.ll.exam.finalproject.app.member.controller;

import com.ll.exam.finalproject.app.base.dto.RsData;
import com.ll.exam.finalproject.app.base.rq.Rq;
import com.ll.exam.finalproject.app.member.dto.LoginRequest;
import com.ll.exam.finalproject.app.member.dto.LoginResponse;
import com.ll.exam.finalproject.app.member.dto.MeResponse;
import com.ll.exam.finalproject.app.member.dto.MemberDto;
import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.member.service.MemberService;
import com.ll.exam.finalproject.app.security.dto.MemberContext;
import com.ll.exam.finalproject.util.Ut;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.ALL_VALUE;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "ApiV1MemberController", description = "로그인 기능과 로그인 된 회원의 정보를 제공 기능을 담당하는 컨트롤러")
public class ApiV1MemberController {
    private final Rq rq;
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    @PostMapping("/login")
    @Operation(summary = "로그인, 엑세스 토큰 발급")
    public ResponseEntity<RsData<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Member member = memberService.findByUsername(loginRequest.getUsername()).orElse(null);

        if (member == null) {
            return Ut.sp.responseEntityOf(RsData.of("F-2", "해당 회원이 존재하지 않습니다."), null);
        }

        if (passwordEncoder.matches(loginRequest.getPassword(), member.getPassword()) == false) {
            return Ut.sp.responseEntityOf(RsData.of("F-3", "비밀번호가 올바르지 않습니다."), null);
        }

        String accessToken = memberService.getAccessToken(member);

        return Ut.sp.responseEntityOf(
                RsData.of(
                        "S-1",
                        "로그인 성공, Access Token을 발급합니다.",
                        new LoginResponse(accessToken)
                )
        );
    }

    @Operation(summary = "로그인된 사용자의 정보", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/me", consumes = ALL_VALUE)
    public ResponseEntity<RsData<MeResponse>> me(@AuthenticationPrincipal MemberContext memberContext){
        if (rq.isLogout()) { // 임시코드, 나중에는 시프링 시큐리티를 이용해서 로그인을 안했다면, 아예 여기로 못 들어오도록
            return Ut.sp.responseEntityOf(
                    RsData.failOf(
                            null
                    )
            );
        }

        return Ut.sp.responseEntityOf(
                RsData.successOf(
                        new MeResponse(MemberDto.of(rq.getMember()))
                )
        );
    }

}
