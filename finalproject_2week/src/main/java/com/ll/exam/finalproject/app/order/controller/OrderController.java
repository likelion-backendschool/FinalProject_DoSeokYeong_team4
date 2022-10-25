package com.ll.exam.finalproject.app.order.controller;

import com.ll.exam.finalproject.app.base.rq.Rq;
import com.ll.exam.finalproject.app.order.entity.Order;
import com.ll.exam.finalproject.app.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
@PreAuthorize("isAuthenticated()")
public class OrderController {
    private final OrderService orderService;
    private final Rq rq;

    @PostMapping("/create")
    @ResponseBody
    public String orderCreate() { // 주문 생성
        Order order = orderService.createFromCart(rq.getMember());

        return "성공";
//        return String.format("redirect:/order/%d", order.getId());
    }

    @GetMapping("/list")
    public String showOrderList(Model model) { // 주문리스트
        List<Order> orderList = orderService.findByMemberId(rq.getMember());

        model.addAttribute("orderList", orderList);

        return "order/list";
    }

    @GetMapping("/{id}")
    public String showOrderDetail(@PathVariable long id, Model model) {
        Order order = orderService.findById(id);

        model.addAttribute("order", order);

        return "order/detail";
    }

}
