package com.ll.exam.finalproject.app.cart.controller;

import com.ll.exam.finalproject.app.base.dto.RsData;
import com.ll.exam.finalproject.app.base.rq.Rq;
import com.ll.exam.finalproject.app.cart.entity.CartItem;
import com.ll.exam.finalproject.app.cart.service.CartService;
import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.product.entity.Product;
import com.ll.exam.finalproject.app.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
@Slf4j
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final Rq rq;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showCartList(Model model) {
        Member member = rq.getMember();

        List<CartItem> cartItemList = cartService.findByMemberId(member.getId());

        model.addAttribute("cartItemList", cartItemList);

        return "cart/list";
    }

    @PostMapping("/add/{productId}")
    @PreAuthorize("isAuthenticated()")
    public String add(@PathVariable long productId) {
        Member member = rq.getMember();
        Product product = productService.findById(productId).get();

        CartItem cartItem = cartService.addItem(member, product);

        return rq.redirectWithMsg("/product/list", "품목이 추가됐습니다");
    }

    @PostMapping("/remove/{productId}")
    @PreAuthorize("isAuthenticated()")
    public String remove(@PathVariable long productId) {
        CartItem cartItem = cartService.findByMemberIdAndProductId(rq.getMember().getId(), productId).orElse(null);

        cartService.deleteItem(cartItem);
        return rq.redirectWithMsg("/cart/list", "품목이 삭제됐습니다");
    }
}
