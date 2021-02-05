package com.vlados.controller;

import com.vlados.entity.User;
import com.vlados.service.OrderService;
import com.vlados.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/user")
    public String getUser(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("user", currentUser);
        model.addAttribute("orders", orderService.getOrdersByUser(currentUser));
        return "user";
    }

    @PostMapping("/user/lock/{user}")
    public String lockUser(@PathVariable User user){
        userService.lockUser(user);
        return "redirect:/users";
    }

    @PostMapping("/user/unlock/{user}")
    public String unlockUser(@PathVariable User user) {
        userService.unlockUser(user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getUsers());
        return "users";
    }
}
