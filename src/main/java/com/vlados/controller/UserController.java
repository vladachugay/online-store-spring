package com.vlados.controller;

import com.vlados.dto.UserDTO;
import com.vlados.entity.Order;
import com.vlados.entity.OrderStatus;
import com.vlados.entity.Role;
import com.vlados.entity.User;
import com.vlados.exception.StoreException;
import com.vlados.service.OrderService;
import com.vlados.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class  UserController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/registration")
    public String getRegistration(@ModelAttribute UserDTO userDTO) {
        return "registration";
    }

    @PostMapping("/registration")
    public String register(@Valid @ModelAttribute UserDTO userDTO, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors())
            return "registration";

        try {
            userService.saveUser(userDTO);
        } catch (StoreException e) {
            model.addAttribute("error_msg", e.getMessage());
            return "registration";
        }
        return "redirect:/login";

    }

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

    @GetMapping("/admin")
    public String getAdminPanel(Model model, @RequestParam(required = false) Optional<Integer> page,
                                @RequestParam(required = false) Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Order> orderPage = orderService.getOrders(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("orderPage", orderPage);

        int totalPages = orderPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("statuses", OrderStatus.values());
        return "adminPanel";
    }
}
