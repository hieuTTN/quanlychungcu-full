package com.web.controller;

import com.web.entity.Resident;
import com.web.entity.User;
import com.web.repository.ResidentRepository;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private ResidentRepository residentRepository;

    @GetMapping(value = {"/login", "/"})
    public String loginView(){
        return "login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String username, @RequestParam String password, RedirectAttributes redirectAttributes, HttpSession session){
        Optional<Resident> resident = residentRepository.findByUsernameAndPassword(username, password);
        if(resident.isEmpty()){
            redirectAttributes.addFlashAttribute("error", "Tài khoản hoặc mật khẩu không chính xác!");
            return "redirect:/";
        }
        session.setAttribute("resident", resident.get());
        return "redirect:/thongtin";
    }
}
