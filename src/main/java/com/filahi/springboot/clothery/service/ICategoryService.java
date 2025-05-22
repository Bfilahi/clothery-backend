package com.filahi.springboot.clothery.service;

import com.filahi.springboot.clothery.entity.Category;
import com.filahi.springboot.clothery.exception.domain.NotTheCorrectImageFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ICategoryService {
    Category addNewCategory(String categoryName, String type, MultipartFile image, Character gender) throws IOException, NotTheCorrectImageFileException;
    Category updateCategory(String categoryName, String type, MultipartFile image, Character gender) throws IOException, NotTheCorrectImageFileException;
    void deleteCategory(String type, Character gender) throws IOException;
}
