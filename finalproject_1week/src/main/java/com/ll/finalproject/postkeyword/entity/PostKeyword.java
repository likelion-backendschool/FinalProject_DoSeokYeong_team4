package com.ll.finalproject.postkeyword.entity;

import com.ll.finalproject.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class PostKeyword extends BaseEntity {
    private String content; // ํด์ํ๊ทธ
}