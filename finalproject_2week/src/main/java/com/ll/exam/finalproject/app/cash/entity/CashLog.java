package com.ll.exam.finalproject.app.cash.entity;

import com.ll.exam.finalproject.app.base.entity.BaseEntity;
import com.ll.exam.finalproject.app.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class CashLog extends BaseEntity {

    @ManyToOne(fetch = LAZY)
    private Member member;

    private Long price; // 변동 가격

    private String eventType; // 변동 종류
}
