package com.filahi.springboot.clothery.service;

import com.filahi.springboot.clothery.entity.Product;
import com.filahi.springboot.clothery.exception.domain.NotTheCorrectImageFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface IProductService {
    Product addNewProduct(
            String productName,
            String description,
            BigDecimal price,
            String category,
            String sizes,
            MultipartFile[] images) throws NotTheCorrectImageFileException, IOException;

    List<Product> getProducts(Character gender);

    Product getProduct(Long id);

    List<Product> getProductsByCategory(Long id);

    List<Product> searchProducts(String keyword);

    void deleteProduct(Long id) throws IOException;

    Product updateProduct(Long id,
                                 String productName,
                                 String description,
                                 BigDecimal price,
                                 String categoryId,
                                 String sizesJson,
                                 MultipartFile[] images) throws IOException;
}
