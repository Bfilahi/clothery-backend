package com.filahi.springboot.clothery.controller;

import com.filahi.springboot.clothery.dto.ProductResponseDTO;
import com.filahi.springboot.clothery.entity.Category;
import com.filahi.springboot.clothery.entity.Hero;
import com.filahi.springboot.clothery.service.CategoryService;
import com.filahi.springboot.clothery.service.HeroService;
import com.filahi.springboot.clothery.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api")
public class CustomerController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final HeroService heroService;


    @Autowired
    public CustomerController(ProductService productService, CategoryService categoryService, HeroService heroService){
        this.productService = productService;
        this.categoryService = categoryService;
        this.heroService = heroService;
    }


    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories(){
        List<Category> categories = this.categoryService.getCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/hero")
    public ResponseEntity<Hero> getHeroImages(){
        Hero hero = this.heroService.getHeroImages();
        return new ResponseEntity<>(hero, HttpStatus.OK);
    }

    @GetMapping("/products/{gender}")
    public ResponseEntity<List<ProductResponseDTO>> getProducts(@PathVariable("gender") String gender){
        char g;
        if(gender.equals("men"))
            g = 'M';
        else if(gender.equals("women"))
            g = 'F';
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<ProductResponseDTO> products = this.productService.getProducts(g);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable("productId") long productId){
        ProductResponseDTO product = this.productService.getProduct(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProduct(@RequestParam("keyword") String keyword){
        List<ProductResponseDTO> products = this.productService.searchProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/products/category")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@RequestParam("id") long categoryId){
        List<ProductResponseDTO> products = this.productService.getProductsByCategory(categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
