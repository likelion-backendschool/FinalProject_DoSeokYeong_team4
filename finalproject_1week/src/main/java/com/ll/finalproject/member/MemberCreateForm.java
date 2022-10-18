package com.ll.finalproject.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberCreateForm {

    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    private String username;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    private String password;

    @NotEmpty(message = "비밀번호확인은 필수항목입니다.")
    private String passwordConfirm;


    private String nickname;

    @Email
    @NotEmpty(message = "이메일은 필수항목입니다.")
    private String email;
}
