package com.vlados.controller;

import com.vlados.dto.UserDTO;
import com.vlados.entity.Role;
import com.vlados.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/")
    public String getMainPage() {
        return "main";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String register(UserDTO userDTO){
        userDTO.setActive(true);
        userDTO.setRole(Role.USER.name());
        userService.saveUser(userDTO);
        return "redirect:/login";
    }


    @GetMapping("/shop")
    public String getShop() {
        return "shop";
    }



}
