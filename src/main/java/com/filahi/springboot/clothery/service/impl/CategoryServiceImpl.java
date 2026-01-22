package com.filahi.springboot.clothery.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.filahi.springboot.clothery.entity.Category;
import com.filahi.springboot.clothery.repository.CategoryRepository;

import com.filahi.springboot.clothery.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final Cloudinary cloudinary;


    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, Cloudinary cloudinary){
        this.categoryRepository = categoryRepository;
        this.cloudinary = cloudinary;
    }


    @Override
    public List<Category> getCategories() {
        return this.categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category addNewCategory(String categoryName, String type, MultipartFile image, Character gender) throws IOException {
        Category category = new Category();
        category.setId(0);
        category.setCategoryName(categoryName);
        category.setType(type);
        category.setGender(gender);

        if(image != null && !image.isEmpty()) {
            Map<?, ?> result = uploadImage(image, category);
            category.setImgUrl((String) result.get("url"));
            category.setPublicId((String) result.get("public_id"));
        }

        this.categoryRepository.save(category);
        return category;
    }

    @Override
    @Transactional
    public Category updateCategory(long categoryId, String categoryName, String type, MultipartFile image, Character gender) throws IOException {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if(category.getPublicId() != null)
            this.cloudinary.uploader().destroy(category.getPublicId(), ObjectUtils.asMap("folder", "clothery"));

        if(image != null && !image.isEmpty()){
            Map<?, ?> result = uploadImage(image, category);
            category.setImgUrl((String) result.get("url"));
            category.setPublicId((String) result.get("public_id"));
        }

        category.setCategoryName(categoryName);
        category.setType(type);
        category.setGender(gender);

        this.categoryRepository.save(category);
        return category;
    }

    @Override
    @Transactional
    public void deleteCategory(long categoryId) throws IOException {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if(category.getPublicId() != null)
            this.cloudinary.uploader().destroy(category.getPublicId(), ObjectUtils.emptyMap());

        this.categoryRepository.delete(category);
    }

    private Map<?, ?> uploadImage(MultipartFile image, Category category) throws IOException {
            try{
                return (Map<?, ?>) this.cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                        "folder", "clothery"
                ));
            }catch (IOException exception){
                throw new IOException("Failed to upload image to Cloudinary.", exception);
            }
    }
}