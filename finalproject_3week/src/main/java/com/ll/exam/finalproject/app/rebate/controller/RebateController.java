package com.ll.exam.finalproject.app.rebate.controller;

import com.ll.exam.finalproject.app.base.dto.RsData;
import com.ll.exam.finalproject.app.base.rq.Rq;
import com.ll.exam.finalproject.app.cart.entity.CartItem;
import com.ll.exam.finalproject.app.member.entity.Member;
import com.ll.exam.finalproject.app.rebate.entity.RebateOrderItem;
import com.ll.exam.finalproject.app.rebate.service.RebateService;
import com.ll.exam.finalproject.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/adm/rebate")
@RequiredArgsConstructor
public class RebateController {
    private final RebateService rebateService;
    private final Rq rq;

    @GetMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMakeData() {
        return "adm/home/makeData";
    }

    @PostMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createMakeData(@RequestParam String yearMonth, RedirectAttributes redirectAttributes) {
        // yearMonth로 년, 월값이 넘어옴 ex) 2022-03
        rebateService.makeDate(yearMonth);

        redirectAttributes.addAttribute("yearMonth", yearMonth); // redirect url에 yearMonth를 포함하여 전송
        return rq.redirectWithMsg("/adm/rebate/rebateOrderItemList", "목록이 생성됐습니다.");
    }

    @GetMapping("/rebateOrderItemList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRebateOrderItemList(String yearMonth, Model model) {

        List<RebateOrderItem> items = rebateService.findRebateOrderItemsByPayDateIn(yearMonth);

        model.addAttribute("items", items);

        return "adm/rebate/list";
    }

    @GetMapping("/loadData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String loadRebateData() {
        return "adm/home/loadData";
    }

    @PostMapping("/rebate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebateAll(String ids, HttpServletRequest req) {

        String[] idsArr = ids.split(",");

        Arrays.stream(idsArr)
                .mapToLong(Long::parseLong)
                .forEach(id -> {
                    rebateService.rebate(id);
                });

        String referer = req.getHeader("Referer");

        String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");

        String redirect = "redirect:/adm/rebate/rebateOrderItemList?yearMonth=" + yearMonth;
        redirect += "&msg=" + Ut.url.encode("%d건의 정산품목을 정산처리하였습니다.".formatted(idsArr.length));

        return redirect;
    }

    @PostMapping("/rebateOne/{rebateOrderItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebateOne(@PathVariable Long rebateOrderItemId, HttpServletRequest req) {

        RsData rebateRsData = rebateService.rebate(rebateOrderItemId);

        String referer = req.getHeader("Referer");

        String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");

        String redirect = "redirect:/adm/rebate/rebateOrderItemList?yearMonth=" + yearMonth;

        redirect = rebateRsData.addMsgToUrl(redirect);

        return redirect;
    }
}
