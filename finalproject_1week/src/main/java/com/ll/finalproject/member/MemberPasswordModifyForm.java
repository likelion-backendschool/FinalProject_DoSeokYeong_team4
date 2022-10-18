package com.ll.finalproject.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberPasswordModifyForm {
    @NotEmpty
    private String oldPassword;

    @NotEmpty
    private String password;

    @NotEmpty
    private String passwordConfirm;
}