package com.ll.exam.finalproject.app.rebate.controller;

import com.ll.exam.finalproject.app.rebate.entity.RebateOrderItem;
import com.ll.exam.finalproject.app.rebate.service.RebateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/adm/rebate")
@RequiredArgsConstructor
public class RebateController {
    private final RebateService rebateService;

    @GetMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMakeData() {
        return "adm/home/makeData";
    }

    @PostMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public String createMakeData(@RequestParam String yearMonth) {
        // yearMonth로 년, 월값이 넘어옴 ex) 2022-03
        rebateService.makeDate(yearMonth);

        return "성공";
    }

    @GetMapping("/rebateOrderItemList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRebateOrderItemList(@RequestParam String yearMonth, Model model) {
        if (yearMonth == null) {
            yearMonth = "2022-11";
        }
        List<RebateOrderItem> items = rebateService.findRebateOrderItemsByPayDateIn(yearMonth);

        model.addAttribute("items", items);

        return "adm/rebate/list";
    }
}
