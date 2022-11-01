package com.ll.exam.finalproject.app.rebate.controller;

import com.ll.exam.finalproject.app.base.dto.RsData;
import com.ll.exam.finalproject.app.base.rq.Rq;
import com.ll.exam.finalproject.app.rebate.entity.RebateOrderItem;
import com.ll.exam.finalproject.app.rebate.service.RebateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/rebate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebateAll() {
        return null;
    }

    @PostMapping("/rebateOne/{rebateOrderItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public String rebateOne(@PathVariable Long rebateOrderItemId) {

        RsData rebateRsData = rebateService.rebate(rebateOrderItemId);

        return rebateRsData.getMsg();
    }
}
