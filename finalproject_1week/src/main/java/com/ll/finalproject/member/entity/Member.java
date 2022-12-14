package com.ll.finalproject.member.entity;

import com.ll.finalproject.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class Member extends BaseEntity {

    @Column(unique = true)
    private String username; // 로그인 아이디

    private String password; // 로그인 비밀번호

    @Column(unique = true)
    private String nickname; // 필명

    private String email; // 이메일

    private int authLevel; // 권한 레벨 3 = 일반, 7 = 관리자

}
