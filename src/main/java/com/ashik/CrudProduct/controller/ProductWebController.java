package com.ashik.CrudProduct.controller;

import com.ashik.CrudProduct.model.Product;
import com.ashik.CrudProduct.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductWebController {

    @Autowired
    private ProductService productService;


    @GetMapping
    public String listProducts(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String status,
                               Model model) {
        int size = 5;
        Page<Product> productPage;

        boolean isFiltered = (keyword != null && !keyword.isBlank()) || (status != null && !status.isBlank());

        if (isFiltered) {
            productPage = productService.searchProducts(keyword, status, PageRequest.of(page, size));
        } else {
            productPage = productService.getPaginatedProducts(page, size);
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        return "products";
    }




//    @GetMapping("/do")
//    public String listProducts(@RequestParam(defaultValue = "0") int page,
//                               @RequestParam(required = false) String keyword,
//                               Model model) {
//        int size = 5;
//        Page<Product> productPage;
//        if (keyword != null && !keyword.isBlank()) {
//            productPage = productService.searchProducts(keyword, PageRequest.of(page, size));
//        } else {
//            productPage = productService.getPaginatedProducts(page, size);
//        }
//        model.addAttribute("productPage", productPage);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("keyword", keyword);
//        return "products";
//    }
//    public String listProducts(
//            @RequestParam(defaultValue = "0") int page,
//            Model model) {
//
//        Page<Product> productPage = productService.getPaginatedProducts(page, 5);
//        model.addAttribute("productPage", productPage);
//        model.addAttribute("currentPage", page);
//        return "products";
//    }


//    @GetMapping
//    public String list(Model model) {
//        model.addAttribute("products", productService.findAlls());
//        return "products";
//    }

    @GetMapping("/post")
    public String createForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    @PostMapping
    public String save(@ModelAttribute Product product) {
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        return "product-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/products";
    }
}
