package com.ll.finalproject.member.controller;

import com.ll.finalproject.mail.MailService;
import com.ll.finalproject.member.entity.Member;
import com.ll.finalproject.member.MemberCreateForm;
import com.ll.finalproject.member.MemberModifyForm;
import com.ll.finalproject.member.MemberPasswordModifyForm;
import com.ll.finalproject.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final MailService mailService;

    // 회원 가입
    @GetMapping("/join")
    public String showJoin() {
        return "member/join";
    }

    @PostMapping("/join")
    public String doJoin(@Valid MemberCreateForm memberCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "member/join";
        }
        if (!memberCreateForm.getPassword().equals(memberCreateForm.getPasswordConfirm())) {
            return "member/join";
        }

        if (memberCreateForm.getNickname().equals("") || memberCreateForm.getNickname().trim().length() < 1) { // 닉네임 설정 안한 경우 권한 레벨 3
            memberService.join(memberCreateForm.getUsername(), memberCreateForm.getPassword(), memberCreateForm.getEmail());
        } else {
            memberService.join(memberCreateForm.getUsername(), memberCreateForm.getPassword(), memberCreateForm.getNickname(),
                    memberCreateForm.getEmail());
        }
        mailService.sendMail(memberCreateForm.getEmail(), "회원 가입을 축하합니다",
                memberCreateForm.getUsername() + "님의 회원 가입을 축하합니다");

        return "redirect:/msg=joinSuccess";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "member/login";
    }

    // 회원 정보 수정
    @GetMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String showModify(Model model, Principal principal) {
        Member loggedInMember = memberService.findByUsername(principal.getName());

        model.addAttribute("loggedInMember", loggedInMember);

        return "member/modify";
    }

    @PostMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String doModify(@Valid MemberModifyForm memberModifyForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "member/modify";
        }

        Member member = memberService.findByUsername(principal.getName());

        // 닉네임 설정 안한 경우
        if (memberModifyForm.getNickname().equals("") || memberModifyForm.getNickname().trim().length() < 1) {
            memberService.modify(member, memberModifyForm.getEmail());
        } else {
            memberService.modify(member, memberModifyForm.getEmail(), memberModifyForm.getNickname());
        }


        return "redirect:/?msg=modifySuccess";
    }

    // 회원 비밀번호 수정
    @GetMapping("/modifyPassword")
    @PreAuthorize("isAuthenticated()")
    public String showModifyPassword() {

        return "member/modifyPassword";
    }

    @PostMapping("/modifyPassword")
    @PreAuthorize("isAuthenticated()")
    public String doModifyPassword(@Valid MemberPasswordModifyForm memberPasswordModifyForm, BindingResult bindingResult,
                                   Principal principal) {
        Member member = memberService.findByUsername(principal.getName());

        if (bindingResult.hasErrors()) {
            return "member/modifyPassword";
        }
        // 1. 이전 비밀번호를 알고있는가
        // 2. 이전 비밀번호와 다른가
        // 3. 새 비밀번호 일치 확인을 했는가
        if (passwordEncoder.matches(member.getPassword(), memberPasswordModifyForm.getOldPassword()) &&
                !memberPasswordModifyForm.getOldPassword().equals(memberPasswordModifyForm.getPassword()) &&
                memberPasswordModifyForm.getPassword().equals(memberPasswordModifyForm.getPasswordConfirm())) {
            return "member/modifyPassword";
        }

        memberService.modifyPassword(member, memberPasswordModifyForm.getPassword());

        return "redirect:/?msg=modifyPasswordSuccess";
    }

    // 회원 아이디 찾기
    @GetMapping("/findUsername")
    public String showFindUsername() {

        return "member/findUsername";
    }

    @PostMapping("/findUsername")
    @ResponseBody
    public String doFindUsername(@RequestParam(name = "email") String email){
        Member member = memberService.findByEmail(email);
        if (member == null) {
            return "해당 회원이 존재하지 않습니다";
        } else {
            return member.getUsername();
        }
    }

    // 회원 비밀번호 찾기
    @GetMapping("/findPassword")
    public String showFindPassword() {

        return "member/findPassword";
    }

    @PostMapping("/findPassword")
    @ResponseBody
    public String doFindPassword(@RequestParam(name = "username") String username, @RequestParam(name = "email") String email) {
        Member member = memberService.findByUsernameAndEmail(username, email);
        if (member == null) {
            return "해당 회원이 존재하지 않습니다.";
        }

        String newPassword = UUID.randomUUID().toString().substring(0, 8);

        mailService.sendMail(email, "회원님의 임시비밀번호입니다.",
                member.getUsername() + "회원님의 임시비밀번호는" + newPassword + "입니다.");

        memberService.modifyPassword(member, newPassword);

        return "입력하신 이메일로 임시 비밀번호가 발송됐습니다";
    }
}
