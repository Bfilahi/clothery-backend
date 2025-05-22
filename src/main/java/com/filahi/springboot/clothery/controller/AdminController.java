package com.filahi.springboot.clothery.controller;


import com.filahi.springboot.clothery.domain.HttpResponse;
import com.filahi.springboot.clothery.dto.ProductResponseDTO;

import com.filahi.springboot.clothery.entity.Category;
import com.filahi.springboot.clothery.entity.Hero;
import com.filahi.springboot.clothery.entity.Product;
import com.filahi.springboot.clothery.exception.domain.NotTheCorrectImageFileException;
import com.filahi.springboot.clothery.service.ICategoryService;
import com.filahi.springboot.clothery.service.IHeroService;
import com.filahi.springboot.clothery.service.IProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;


@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ICategoryService ICategoryService;
    private final IProductService IProductService;
    private final IHeroService IHeroService;
    private static final String CATEGORY_DELETED_SUCCESSFULLY = "Category deleted successfully";
    private static final String PRODUCT_DELETED_SUCCESSFULLY = "Product deleted successfully";


    @Autowired
    public AdminController(ICategoryService ICategoryService,
                           IProductService IProductService,
                           IHeroService IHeroService){

        this.ICategoryService = ICategoryService;
        this.IProductService = IProductService;
        this.IHeroService = IHeroService;
    }


    @PostMapping("/category/add")
    public ResponseEntity<Category> addNewCategory(@RequestParam("categoryName") String categoryName,
                                                   @RequestParam("type") String type,
                                                   @RequestParam("imgUrl") MultipartFile image,
                                                   @RequestParam("gender") Character gender) throws IOException, NotTheCorrectImageFileException {

        Category theCategory = this.ICategoryService.addNewCategory(categoryName, type, image, gender);
        return new ResponseEntity<>(theCategory, HttpStatus.OK);
    }

    @DeleteMapping("/category/{type}/{gender}")
    public ResponseEntity<HttpResponse> deleteCategory(@PathVariable("type") String type, @PathVariable("gender") Character gender) throws IOException {
        this.ICategoryService.deleteCategory(type, gender);
        return response(HttpStatus.OK, CATEGORY_DELETED_SUCCESSFULLY);
    }

    @PutMapping("/category/update")
    public ResponseEntity<Category> updateCategory(@RequestParam("categoryName") String categoryName,
                                                       @RequestParam("type") String type,
                                                       @RequestParam("imgUrl") MultipartFile image,
                                                       @RequestParam("gender") Character gender) throws IOException, NotTheCorrectImageFileException {

        Category theCategory = this.ICategoryService.updateCategory(categoryName, type, image, gender);
        return new ResponseEntity<>(theCategory, HttpStatus.OK);
    }




    @PostMapping("/product/add")
    public ResponseEntity<ProductResponseDTO> addNewProduct(@RequestParam("productName") String productName,
                                                            @RequestParam("description") String description,
                                                            @RequestParam("price") BigDecimal price,
                                                            @RequestParam("categoryId") String categoryId,
                                                            @RequestParam("sizes") String sizes,
                                                            @RequestParam("image") MultipartFile[] images) throws IOException, NotTheCorrectImageFileException {

        Product theProduct = this.IProductService.addNewProduct(productName, description, price, categoryId, sizes, images);
        return new ResponseEntity<>(new ProductResponseDTO(theProduct), HttpStatus.OK);
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<HttpResponse> deleteProduct(@PathVariable("id") Long id) throws IOException {
        this.IProductService.deleteProduct(id);
        return response(HttpStatus.OK, PRODUCT_DELETED_SUCCESSFULLY);
    }

    @PutMapping(value = "/product/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable("id") Long id,
                                                 @RequestParam("productName") String productName,
                                                 @RequestParam("description") String description,
                                                 @RequestParam("price") BigDecimal price,
                                                 @RequestParam("categoryId") String categoryId,
                                                 @RequestParam("sizes") String sizesJson,
                                                 @RequestParam("image") MultipartFile[] images) throws IOException {


        Product theProduct = this.IProductService.updateProduct(id, productName, description, price, categoryId, sizesJson, images);

        return new ResponseEntity<>(new ProductResponseDTO(theProduct), HttpStatus.OK);
    }


    @PostMapping("/hero/add")
    public ResponseEntity<Hero> addHeroImages(@RequestParam("left_image") MultipartFile leftImage,
                                              @RequestParam("right_image") MultipartFile rightImage) throws IOException {

        Hero hero = this.IHeroService.addHeroImages(leftImage, rightImage);

        return new ResponseEntity<>(hero, HttpStatus.OK);
    }


    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message){
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }
}