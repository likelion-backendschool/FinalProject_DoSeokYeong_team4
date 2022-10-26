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
    public String orderCreate() { // 주문 생성
        Order order = orderService.createFromCart(rq.getMember());

        return String.format("redirect:/order/list");
    }

    @GetMapping("/list")
    public String showOrderList(Model model) { // 주문리스트
        List<Order> orderList = orderService.findAllByMemberId(rq.getMember().getId());

        model.addAttribute("orderList", orderList);

        return "order/list";
    }

    @GetMapping("/{id}")
    public String showOrderDetail(@PathVariable long id, Model model) {
        Order order = orderService.findById(id);

        model.addAttribute("order", order);

        return "order/detail";
    }

    @PostMapping("/{id}/cancel")
    public String orderCancel(@PathVariable long id) { // 주문 생성
        Order order = orderService.findById(id);
        orderService.deleteOrder(order);

        return String.format("redirect:/order/list");
    }

}
