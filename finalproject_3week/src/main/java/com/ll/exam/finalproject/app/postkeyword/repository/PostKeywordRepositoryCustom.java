package com.ll.exam.finalproject.app.postkeyword.repository;

import com.ll.exam.finalproject.app.postkeyword.entity.PostKeyword;

import java.util.List;

public interface PostKeywordRepositoryCustom {
    List<PostKeyword> getQslAllByAuthorId(Long authorId);
}
