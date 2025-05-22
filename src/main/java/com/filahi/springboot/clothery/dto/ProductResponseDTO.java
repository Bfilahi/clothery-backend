package com.filahi.springboot.clothery.dto;


import com.filahi.springboot.clothery.entity.Image;
import com.filahi.springboot.clothery.entity.Product;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ProductResponseDTO {
    private final Long id;
    private final String productName;
    private final String description;
    private final Character gender;
    private final BigDecimal unitPrice;
    private final int unitsInStock;
    private final Long categoryId;
    private final List<SizeQuantityDTO> sizes;
    private final List<String> images;

    public ProductResponseDTO(Product product){
        this.id = product.getId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.gender = product.getGender();
        this.unitPrice = product.getUnitPrice();
        this.unitsInStock = product.getUnitsInStock();
        this.categoryId = product.getCategory().getId();
        this.sizes = product.getSizes().stream().map(size -> new SizeQuantityDTO(size.getSize(), size.getQuantity())).collect(Collectors.toList());
        this.images = product.getImages().stream().map(Image::getImgUrl).toList();
    }

    public static List<ProductResponseDTO> fromProducts(List<Product> products){
        return products.stream().map(ProductResponseDTO::new).collect(Collectors.toList());
    }
}
