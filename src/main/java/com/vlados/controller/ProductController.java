package com.vlados.controller;

import com.vlados.dto.ProductDTO;
import com.vlados.entity.Material;
import com.vlados.entity.Product;
import com.vlados.entity.ProductCategory;
import com.vlados.entity.SortCriteria;
import com.vlados.repository.ProductRepository;
import com.vlados.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping
    public String getProducts(Model model, @RequestParam(required = false) Optional<Integer> page,
                              @RequestParam(required = false) Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(9);
        Page<Product> productPage = productService.getProducts(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("productPage", productPage);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("sorting", SortCriteria.values());
        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("materials", Material.values());
        model.addAttribute("currentPage", currentPage);
        return "products";
    }

    @GetMapping("/add")
    public String productAddForm(Model model) {
        model.addAttribute("categories", ProductCategory.values());


        model.addAttribute("materials", Material.values());
        return "addProduct";
    }

    @PostMapping("/add")
    public String addProduct(ProductDTO productDTO,
                             @RequestParam("file") MultipartFile file) throws IOException {
//        if (file != null && !file.getOriginalFilename().isEmpty()) {
//
//            String uuidFile = UUID.randomUUID().toString();
//            String resultFilename = uuidFile + "." + file.getOriginalFilename();
//
//            file.transferTo(new File(resultFilename));
//
//            productDTO.setPicPath(resultFilename);
//        }
        productDTO.setDate(LocalDateTime.now());
        //TODO save picture to add directory
        //TODO validate product
        productService.saveProduct(new Product(productDTO));
        return "redirect:/products";
    }

    @GetMapping("/edit/{product}")
    public String productEditForm(@PathVariable Product product, Model model) {
        model.addAttribute("product", product);
        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("materials", Material.values());

        return "editProduct";
    }

    @PostMapping("edit/{product}")
    public String editProduct(@PathVariable Product product, @RequestParam("file") MultipartFile file,
                              @RequestParam("name") String name){

        //TODO convert file to picPath
        //TODO fill product with new values to update it
        product.setName(name);
        System.out.println(product);
        productService.updateProduct(product);
        return "redirect:/products";
    }

    @PostMapping("/delete/{product}")
    public String deleteProduct(@PathVariable Product product) {
        productService.deleteProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/{product}")
    public String product(@PathVariable Product product, Model model) {
        model.addAttribute("product", product);
        return "product";
    }

    @PostMapping("/filter")
    public String filter(Model model, HttpServletRequest request,
                         @RequestParam(required = false) Optional<Integer> page,
                         @RequestParam(required = false) Optional<Integer> size,
                         @RequestParam(name = "material") String material,
                         @RequestParam(name = "category") String category,
                         @RequestParam(name = "sortcriteria") String sortingCriteria,
                         @RequestParam(name = "price_from", required = false) Optional<BigDecimal> from,
                         @RequestParam(name = "price_to", required = false) Optional<BigDecimal> to) {
        //TODO process nulls
        //TODO avoid duplicates
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(9);
        Page<Product> productPage = productService.getFilteredProducts(PageRequest.of(currentPage - 1, pageSize),
                material, category, from.orElse(new BigDecimal("0")), to.orElse(new BigDecimal(100000)), sortingCriteria);

        model.addAttribute("productPage", productPage);

        int totalPages = productPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("sorting", SortCriteria.values());
        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("materials", Material.values());
        model.addAttribute("currentPage", currentPage);
        return "products";
    }
}
