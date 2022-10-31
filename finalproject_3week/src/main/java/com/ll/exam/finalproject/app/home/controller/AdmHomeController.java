package com.ll.exam.finalproject.app.home.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/adm")
public class AdmHomeController {
    @GetMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showIndex() {
        return "redirect:/adm/home/main";
    }

    @GetMapping("/home/main")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMain() {
        return "adm/home/main";
    }

    @GetMapping("/rebate/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMakeData() {
        return "adm/home/makeData";
    }

    @PostMapping("/rebate/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public String createMakeData(@RequestParam String yearMonth) {
        // yearMonth로 년, 월값이 넘어옴 ex) 2022-03

        return yearMonth;
    }
}

