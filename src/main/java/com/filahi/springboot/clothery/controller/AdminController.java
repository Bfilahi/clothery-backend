package com.filahi.springboot.clothery.controller;


import com.filahi.springboot.clothery.dto.ProductResponseDTO;

import com.filahi.springboot.clothery.entity.Category;
import com.filahi.springboot.clothery.entity.Hero;
import com.filahi.springboot.clothery.entity.Size;
import com.filahi.springboot.clothery.exception.ExceptionResponse;
import com.filahi.springboot.clothery.exception.domain.NotTheCorrectImageFileException;
import com.filahi.springboot.clothery.service.CategoryService;
import com.filahi.springboot.clothery.service.HeroService;
import com.filahi.springboot.clothery.service.ProductService;

import com.filahi.springboot.clothery.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final HeroService heroService;
    private final SizeService sizeService;
    private static final String CATEGORY_DELETED_SUCCESSFULLY = "Category deleted successfully";
    private static final String PRODUCT_DELETED_SUCCESSFULLY = "Product deleted successfully";
    private static final String SIZE_DELETED_SUCCESSFULLY = "Size deleted successfully";


    @Autowired
    public AdminController(CategoryService CategoryService,
                           ProductService ProductService,
                           HeroService HeroService, SizeService sizeService){

        this.categoryService = CategoryService;
        this.productService = ProductService;
        this.heroService = HeroService;
        this.sizeService = sizeService;
    }


    @PostMapping("/category/add")
    public ResponseEntity<Category> addNewCategory(@RequestParam("categoryName") String categoryName,
                                                   @RequestParam("type") String type,
                                                   @RequestParam("imgUrl") MultipartFile image,
                                                   @RequestParam("gender") Character gender) throws IOException, NotTheCorrectImageFileException {

        Category theCategory = this.categoryService.addNewCategory(categoryName, type, image, gender);
        return new ResponseEntity<>(theCategory, HttpStatus.CREATED);
    }

    @PutMapping("/category/update/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable("categoryId") long categoryId,
                                                   @RequestParam("categoryName") String categoryName,
                                                   @RequestParam("type") String type,
                                                   @RequestParam("imgUrl") MultipartFile image,
                                                   @RequestParam("gender") Character gender) throws IOException, NotTheCorrectImageFileException {

        Category theCategory = this.categoryService.updateCategory(categoryId, categoryName, type, image, gender);
        return new ResponseEntity<>(theCategory, HttpStatus.OK);
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<ExceptionResponse> deleteCategory(@PathVariable("categoryId") long categoryId) throws IOException {
        this.categoryService.deleteCategory(categoryId);
        return response(HttpStatus.OK, CATEGORY_DELETED_SUCCESSFULLY);
    }

    @PostMapping("/product/add")
    public ResponseEntity<ProductResponseDTO> addNewProduct(@RequestParam("productName") String productName,
                                                            @RequestParam("description") String description,
                                                            @RequestParam("price") BigDecimal price,
                                                            @RequestParam("unitsInStock") int unitsInStock,
                                                            @RequestParam("categoryId") long categoryId,
                                                            @RequestParam("sizeIds") List<Long> sizeIds,
                                                            @RequestParam("images") MultipartFile[] images) throws IOException, NotTheCorrectImageFileException {

        ProductResponseDTO product = this.productService.addNewProduct(productName, description, price, unitsInStock, categoryId, sizeIds, images);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping(value = "/product/update/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable("productId") long productId,
                                                            @RequestParam("productName") String productName,
                                                            @RequestParam("description") String description,
                                                            @RequestParam("price") BigDecimal price,
                                                            @RequestParam("unitsInStock") int unitsInStock,
                                                            @RequestParam("categoryId") long categoryId,
                                                            @RequestParam("sizeIds") List<Long> sizeIds,
                                                            @RequestParam("images") MultipartFile[] images) throws IOException {

        ProductResponseDTO product = this.productService.updateProduct(productId, productName, description, price, unitsInStock, categoryId, sizeIds, images);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<ExceptionResponse> deleteProduct(@PathVariable("productId") long productId) throws IOException {
        this.productService.deleteProduct(productId);
        return response(HttpStatus.OK, PRODUCT_DELETED_SUCCESSFULLY);
    }

    @PostMapping("/hero/add")
    public ResponseEntity<Hero> addHeroImages(@RequestParam("left_image") MultipartFile leftImage,
                                              @RequestParam("right_image") MultipartFile rightImage) throws IOException {

        Hero hero = this.heroService.addHeroImages(leftImage, rightImage);
        return new ResponseEntity<>(hero, HttpStatus.CREATED);
    }

    @GetMapping("/sizes")
    public ResponseEntity<List<Size>> getSizes(){
        List<Size> sizes = this.sizeService.getSizes();
        return new ResponseEntity<>(sizes, HttpStatus.OK);
    }

    @PostMapping("/size/add")
    public ResponseEntity<Size> addNewSize(@RequestBody String size){
        Size theSize = this.sizeService.addSize(size);
        return new  ResponseEntity<>(theSize, HttpStatus.CREATED);
    }

    @DeleteMapping("/size/{sizeId}")
    public ResponseEntity<ExceptionResponse> deleteSize(@PathVariable("sizeId") long sizeId){
        this.sizeService.deleteSize(sizeId);
        return response(HttpStatus.OK, SIZE_DELETED_SUCCESSFULLY);
    }

    private ResponseEntity<ExceptionResponse> response(HttpStatus httpStatus, String message){
        return new ResponseEntity<>(new ExceptionResponse(httpStatus.value(), message, System.currentTimeMillis()), httpStatus);
    }
}