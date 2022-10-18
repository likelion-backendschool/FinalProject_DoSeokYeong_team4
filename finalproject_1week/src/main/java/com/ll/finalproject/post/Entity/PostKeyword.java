package com.ll.finalproject.post.Entity;

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
    private String content; // 해시태그
}