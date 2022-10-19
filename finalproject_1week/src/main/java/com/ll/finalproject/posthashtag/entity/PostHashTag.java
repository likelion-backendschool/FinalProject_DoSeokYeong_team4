package com.ll.finalproject.posthashtag.entity;

import com.ll.finalproject.base.BaseEntity;
import com.ll.finalproject.member.Entity.Member;
import com.ll.finalproject.post.entity.Post;
import com.ll.finalproject.postkeyword.entity.PostKeyword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
public class PostHashTag extends BaseEntity {
    @ManyToOne
    private Member memberId;

    @ManyToOne
    private Post postId;

    @ManyToOne
    private PostKeyword postKeyword;
}