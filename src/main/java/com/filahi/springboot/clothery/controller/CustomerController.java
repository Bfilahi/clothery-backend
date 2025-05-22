package com.filahi.springboot.clothery.controller;

import com.filahi.springboot.clothery.dto.ProductResponseDTO;
import com.filahi.springboot.clothery.entity.Product;
import com.filahi.springboot.clothery.service.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api")
public class CustomerController {
    private final IProductService productService;


    @Autowired
    public CustomerController(IProductService productService){
        this.productService = productService;
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

        List<Product> products = this.productService.getProducts(g);

        return new ResponseEntity<>(ProductResponseDTO.fromProducts(products), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable("id") Long id){
        Product theProduct = this.productService.getProduct(id);

        return new ResponseEntity<>(new ProductResponseDTO(theProduct), HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductResponseDTO>> searchProduct(@RequestParam("keyword") String keyword){
        List<Product> products = this.productService.searchProducts(keyword);

        return new ResponseEntity<>(ProductResponseDTO.fromProducts(products), HttpStatus.OK);
    }

    @GetMapping("/products/category")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@RequestParam("id") Long id){
        List<Product> products = this.productService.getProductsByCategory(id);

        return new ResponseEntity<>(ProductResponseDTO.fromProducts(products), HttpStatus.OK);
    }

}
