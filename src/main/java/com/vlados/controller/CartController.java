package com.vlados.controller;

import com.vlados.entity.Cart;
import com.vlados.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CartController {

    private Cart cart;

    @Autowired
    public CartController(Cart cart) {
        this.cart = cart;
    }

    @GetMapping("/cart")
    public String cart(Model model) {

        model.addAttribute("cart", cart.getCartProducts());
        return "cart";
    }

    @PostMapping("/addtocart/{product}")
    public String addToCart(@PathVariable Product product) {
        cart.getCartProducts().add(product);
        //TODO return valid page
        return "redirect:/product";
    }

    @PostMapping("/deletefromcart/{product}")
    public String deleteFromCart(@PathVariable Product product) {
        cart.getCartProducts().remove(product);
        return "redirect:/cart";
    }

}
