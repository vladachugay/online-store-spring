package com.vlados.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Component
@SessionScope
@Getter
@Setter
public class Cart {
    List<Product> cartProducts = new ArrayList<>();
}
