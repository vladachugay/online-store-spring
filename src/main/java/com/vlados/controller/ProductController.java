package com.vlados.controller;

import com.vlados.dto.ProductDTO;
import com.vlados.entity.Material;
import com.vlados.entity.Product;
import com.vlados.entity.ProductCategory;
import com.vlados.entity.SortCriteria;
import com.vlados.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public String getProducts(Model model,
                              @RequestParam(required = false) Optional<Integer> page,
                              @RequestParam(required = false) Optional<Integer> size,
                              @RequestParam(name = "material", defaultValue = "ALL") String material,
                              @RequestParam(name = "category", defaultValue = "ALL") String category,
                              @RequestParam(name = "sortcriteria", defaultValue = "BY_NAME_ASC") String sortCriteria,
                              @RequestParam(name = "price_from", defaultValue = "1") BigDecimal from,
                              @RequestParam(name = "price_to", defaultValue = "100000") BigDecimal to) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(9);

        Page<Product> productPage = productService.getFilteredProducts(PageRequest.of(currentPage - 1, pageSize),
                material, category, from, to, sortCriteria);

        model.addAttribute("productPage", productPage);
        model.addAttribute("material", material);
        model.addAttribute("category", category);
        model.addAttribute("sortcriteria", sortCriteria);
        model.addAttribute("price_from", from);
        model.addAttribute("price_to", to);

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
    public String productAddForm(@ModelAttribute ProductDTO productDTO, Model model) {
        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("materials", Material.values());
        return "addProduct";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute ProductDTO productDTO,
                             BindingResult bindingResult,
                             @RequestParam("file") MultipartFile file) {
        if (bindingResult.hasErrors())
            return "addProduct";
        //TODO save picture
        productService.saveProduct(productDTO);
        return "redirect:/products";
    }

    @GetMapping("/edit/{product}")
    public String productEditForm(@PathVariable Product product, Model model) {
        model.addAttribute("product", product);
        model.addAttribute("categories", ProductCategory.values());
        model.addAttribute("materials", Material.values());

        return "editProduct";
    }

    @PostMapping("edit/{productId}")
    public String editProduct(@ModelAttribute @Valid Product product,
                              BindingResult bindingResult,
                              @PathVariable Long productId,
                              @RequestParam("file") MultipartFile file){
        if (bindingResult.hasErrors())
            return "editProduct";
        //TODO save file
        productService.updateProduct(productId, product);
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

}
