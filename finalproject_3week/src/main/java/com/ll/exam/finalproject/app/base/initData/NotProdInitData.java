package com.ll.exam.finalproject.app.base.initData;

import com.ll.exam.finalproject.app.cart.service.CartService;
import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.member.service.MemberService;
import com.ll.exam.finalproject.app.order.entity.Order;
import com.ll.exam.finalproject.app.order.service.OrderService;
import com.ll.exam.finalproject.app.post.service.PostService;
import com.ll.exam.finalproject.app.product.entity.Product;
import com.ll.exam.finalproject.app.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class NotProdInitData {
    private boolean initDataDone = false;

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            PostService postService,
            ProductService productService,
            CartService cartService,
            OrderService orderService
    ) {
        return args -> {
            if (initDataDone) {
                return;
            }

            initDataDone = true;

            Member member1 = memberService.join("user1", "1234", "user1@test.com", null);
            Member member2 = memberService.join("user2", "1234", "user2@test.com", "홍길순");
            Member admin = memberService.join("admin", "1234", "user3@test.com", "admin");

            postService.write(
                    member1,
                    "자바를 우아하게 사용하는 방법",
                    "# 내용 1",
                    "<h1>내용 1</h1>",
                    "#IT #자바 #카프카"
            );

            postService.write(
                    member1,
                    "자바스크립트를 우아하게 사용하는 방법",
                    """
                            # 자바스크립트는 이렇게 쓰세요.
                                                    
                            ```js
                            const a = 10;
                            console.log(a);
                            ```
                            """.stripIndent(),
                    """
                            <h1>자바스크립트는 이렇게 쓰세요.</h1><div data-language="js" class="toastui-editor-ww-code-block-highlighting"><pre class="language-js"><code data-language="js" class="language-js"><span class="token keyword">const</span> a <span class="token operator">=</span> <span class="token number">10</span><span class="token punctuation">;</span>
                            <span class="token console class-name">console</span><span class="token punctuation">.</span><span class="token method function property-access">log</span><span class="token punctuation">(</span>a<span class="token punctuation">)</span><span class="token punctuation">;</span></code></pre></div>
                                                    """.stripIndent(),
                    "#IT #프론트엔드 #리액트"
            );

            postService.write(member2, "제목 3", "내용 3", "내용 3", "#IT# 프론트엔드 #HTML #CSS");
            postService.write(member2, "제목 4", "내용 4", "내용 4", "#IT #스프링부트 #자바");
            postService.write(member1, "제목 5", "내용 5", "내용 5", "#IT #자바 #카프카");
            postService.write(member1, "제목 6", "내용 6", "내용 6", "#IT #프론트엔드 #REACT");
            postService.write(member2, "제목 7", "내용 7", "내용 7", "#IT# 프론트엔드 #HTML #CSS");
            postService.write(member2, "제목 8", "내용 8", "내용 8", "#IT #스프링부트 #자바");

            Product product1 = productService.create(member1, "상품명1 상품명1 상품명1 상품명1 상품명1 상품명1", 30_000, "카프카", "#IT #카프카");
            Product product2 = productService.create(member2, "상품명2", 40_000, "스프링부트", "#IT #REACT");
            Product product3 = productService.create(member1, "상품명3", 50_000, "REACT", "#IT #REACT");
            Product product4 = productService.create(member2, "상품명4", 60_000, "HTML", "#IT #HTML");


            // member1 물품 추가 후 결제
            cartService.addItem(member1, product1);
            cartService.addItem(member1, product2);
            memberService.addCash(member1, 100_000, "예치금 충전"); // member1 예치금 100,000 설정
            Order order1 = orderService.createFromCart(member1);
            orderService.payByRestCashOnly(order1);

            // member2 물품 추가 후 결제
            cartService.addItem(member2, product2);
            cartService.addItem(member2, product3);
            memberService.addCash(member2, 200_000, "예치금 충전"); // member2 예치금 200,000 설정
            Order order2 = orderService.createFromCart(member2);
            orderService.payByRestCashOnly(order2);

        };
    }
}
