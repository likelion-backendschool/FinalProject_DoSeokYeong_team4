package com.ll.finalproject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SpringBootTest
@Transactional
class PostTest {

    @Transactional
    @DisplayName("keywords 테스트")
    @Test
    void t1() {
        String keywords = "#자바 #스프링부트 #스프링배치";

        HashSet<String> keywordSet = Arrays.stream(keywords.split("#"))
                .parallel().filter(s -> s.trim().length() > 0)
                .map(String::trim)
                .collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);

        System.out.println(keywordSet.toString());
    }

}
