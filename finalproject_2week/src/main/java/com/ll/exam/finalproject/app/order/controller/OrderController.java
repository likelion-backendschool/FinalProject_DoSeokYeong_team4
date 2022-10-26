package com.ll.exam.finalproject.app.order.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.exam.finalproject.app.base.exception.ActorCanNotPayOrderException;
import com.ll.exam.finalproject.app.base.exception.OrderIdNotMatchedException;
import com.ll.exam.finalproject.app.base.exception.OrderNotEnoughRestCashException;
import com.ll.exam.finalproject.app.base.rq.Rq;
import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.order.entity.Order;
import com.ll.exam.finalproject.app.order.service.OrderService;
import com.ll.exam.finalproject.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
@PreAuthorize("isAuthenticated()")
public class OrderController {
    private final OrderService orderService;
    private final Rq rq;
    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${custom.toss.secretKey}")
    private final String SECRET_KEY;

    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

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

        return rq.redirectWithMsg("/order/list", "주문이 취소됐습니다");
    }

    @PostMapping("/{id}/pay")
    public String payOrder(@PathVariable long id) {
        Order order = orderService.findById(id);

        Member member = rq.getMember();

        long restCash = member.getRestCash(); // RestCash 반환 필요

        if (orderService.actorCanPayment(member, order) == false) {
            throw new ActorCanNotPayOrderException();
        }

        orderService.payByRestCashOnly(order);

        return rq.redirectWithMsg("/order/list", "카드결제가 완료되었습니다.");
    }

    @RequestMapping("/{id}/success")
    public String confirmPayment(
            @PathVariable long id,
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam Long amount,
            Model model
    ) throws Exception {

        Order order = orderService.findById(id);

        long orderIdInputed = Long.parseLong(orderId.split("__")[1]);

        if (id != orderIdInputed) {
            throw new OrderIdNotMatchedException();
        }

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));

        Member actor = rq.getMember();
        long restCash = actor.getRestCash();  // RestCash 반환 필요
        long payPriceRestCash = order.calculatePayPrice() - amount;

        if (payPriceRestCash > restCash) {
            throw new OrderNotEnoughRestCashException();
        }

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            orderService.payByTossPayments(order, payPriceRestCash);

            return "redirect:/order/%d?msg=%s".formatted(order.getId(), Ut.url.encode("결제가 완료되었습니다."));
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/fail";
        }
    }

    @RequestMapping("/{id}/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "order/fail";
    }




}
