package com.filahi.springboot.clothery.service;

import com.filahi.springboot.clothery.dto.ProductResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductResponseDTO addNewProduct(
            String productName,
            String description,
            BigDecimal price,
            int unitsInStock,
            long categoryId,
            List<Long> sizeIds,
            MultipartFile[] images) throws IOException;
    ProductResponseDTO updateProduct(
                          long productId,
                          String productName,
                          String description,
                          BigDecimal price,
                          int unitsInStock,
                          long categoryId,
                          List<Long> sizeIds,
                          MultipartFile[] images) throws IOException;
    void deleteProduct(long productId) throws IOException;
    List<ProductResponseDTO> getProducts(Character gender);
    ProductResponseDTO getProduct(long id);
    List<ProductResponseDTO> getProductsByCategory(long id);
    List<ProductResponseDTO> searchProducts(String keyword);
}
