package com.filahi.springboot.clothery.dto;


import com.filahi.springboot.clothery.entity.Category;
import com.filahi.springboot.clothery.entity.Image;

import com.filahi.springboot.clothery.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class ProductResponseDTO {
    private long id;
    private String productName;
    private String description;
    private Character gender;
    private BigDecimal unitPrice;
    private int unitsInStock;
    private Category category;
    private List<Size> sizes;
    private List<Image> images;
}
