package com.ll.finalproject.post;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PostCreateForm {
    @NotEmpty(message = "제목은 필수항목입니다.")
    private String subject;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    private String keywords;
}
