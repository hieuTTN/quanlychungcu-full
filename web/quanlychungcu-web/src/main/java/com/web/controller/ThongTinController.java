package com.web.controller;

import com.web.repository.ResidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThongTinController {

    @Autowired
    private ResidentRepository residentRepository;

    @GetMapping(value = {"/thongtin"})
    public String loginView(){
        return "thongtin";
    }

}
