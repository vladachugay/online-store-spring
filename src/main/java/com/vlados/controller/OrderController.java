package com.vlados.controller;

import com.vlados.entity.Cart;
import com.vlados.entity.Order;
import com.vlados.entity.OrderStatus;
import com.vlados.entity.Product;
import com.vlados.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final Cart cart;
    private final OrderService orderService;

    @GetMapping("/cart")
    public String cart(Model model) {
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("cart/add/{product}")
    public String addToCart(@PathVariable Product product) {
        cart.addProduct(product);
        //TODO return valid page
        return "redirect:/products/" + product.getId();
    }

    @PostMapping("cart/delete/{product}")
    public String deleteFromCart(@PathVariable Product product) {
        cart.deleteProduct(product);
        return "redirect:/cart";
    }

    @PostMapping("orders/create")
    public String createOrder() {
        orderService.createOrder(cart);
        cart.clear();
        return "redirect:/cart";
    }

    @GetMapping("/orders/{order}")
    public String getOrder(@PathVariable Order order, Model model) {
        model.addAttribute("order", order);
        //TODO some logic
        return "order";
    }

    @PostMapping("/orders/pay/{order}")
    public String setPaid(@PathVariable Order order) {
        orderService.setStatusPaid(order);
        return "redirect:/admin";
    }

    @PostMapping("/orders/cancel/{order}")
    public String cancelOrder(@PathVariable Order order) {
        orderService.cancelOrder(order);
        return "redirect:/admin";
    }
}
