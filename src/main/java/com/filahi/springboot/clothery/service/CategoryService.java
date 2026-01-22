package com.filahi.springboot.clothery.service;

import com.filahi.springboot.clothery.entity.Category;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    Category addNewCategory(String categoryName, String type, MultipartFile image, Character gender) throws IOException;
    Category updateCategory(long categoryId, String categoryName, String type, MultipartFile image, Character gender) throws IOException;
    void deleteCategory(long categoryId) throws IOException;

    List<Category> getCategories();
}
