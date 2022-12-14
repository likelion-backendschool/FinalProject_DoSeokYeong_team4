package com.ll.finalproject.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberModifyForm {

    private String nickname;

    @Email
    @NotEmpty(message = "이메일은 필수항목입니다.")
    private String email;
}