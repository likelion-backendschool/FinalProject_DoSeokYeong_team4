package com.ll.exam.finalproject.app.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/adm")
public class AdminController {

    @GetMapping("/home/main")
    public String showAdminPage() {


    }
}
