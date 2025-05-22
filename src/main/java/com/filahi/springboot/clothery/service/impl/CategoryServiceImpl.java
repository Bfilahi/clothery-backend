package com.filahi.springboot.clothery.service.impl;

import com.filahi.springboot.clothery.entity.Category;
import com.filahi.springboot.clothery.exception.domain.NotTheCorrectImageFileException;
import com.filahi.springboot.clothery.repository.ICategoryRepository;

import com.filahi.springboot.clothery.service.ICategoryService;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;


import static com.filahi.springboot.clothery.constant.Constants.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


@Service
public class CategoryServiceImpl implements ICategoryService {
    private final ICategoryRepository ICategoryRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final MediaType IMAGE_AVIF = new MediaType("image", "avif");

    @Autowired
    public CategoryServiceImpl(ICategoryRepository ICategoryRepository){
        this.ICategoryRepository = ICategoryRepository;
    }

    @Override
    @Transactional
    public Category addNewCategory(String categoryName, String type, MultipartFile image, Character gender) throws IOException, NotTheCorrectImageFileException {
        Category existingCategory = this.ICategoryRepository.findByTypeAndGender(type, gender);


        if(existingCategory != null)
            return setAndSave(existingCategory, categoryName, type, image, gender);


        Category theCategory = new Category();
        return setAndSave(theCategory, categoryName, type, image, gender);
    }

    @Override
    @Transactional
    public void deleteCategory(String type, Character gender) throws IOException {
        Category theCategory = this.ICategoryRepository.findByTypeAndGender(type, gender);

        if(theCategory == null)
            throw new NoResultException(CATEGORY_NOT_FOUND);

        Files.deleteIfExists(Paths.get(CATEGORY_FOLDER + getGender(gender) + SEPARATOR + type + SEPARATOR + theCategory.getCategoryName() + DOT + AVIF_EXTENSION));

        this.ICategoryRepository.deleteById(theCategory.getId());
    }


    @Override
    @Transactional
    public Category updateCategory(String categoryName, String type, MultipartFile image, Character gender) throws IOException, NotTheCorrectImageFileException {
        Category theCategory = this.ICategoryRepository.findByTypeAndGender(type, gender);

        if(theCategory == null)
            throw new NoResultException(CATEGORY_NOT_FOUND);

        return setAndSave(theCategory, categoryName, type, image, gender);
    }

    private Category setAndSave(Category theCategory, String categoryName, String type, MultipartFile image, Character gender) throws IOException, NotTheCorrectImageFileException {
        theCategory.setCategoryName(categoryName);
        theCategory.setType(type);
        theCategory.setImgUrl(getTemporaryCategoryImage(image.getContentType()));
        theCategory.setGender(gender);

        saveImage(theCategory, image);
        this.ICategoryRepository.save(theCategory);

        return theCategory;
    }

    private String getTemporaryCategoryImage(String categoryType) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(DEFAULT_CATEGORY_IMAGE_PATH + categoryType).toUriString();
    }

    private void saveImage(Category category, MultipartFile image) throws NotTheCorrectImageFileException, IOException {
        if(image != null){
            if(!Objects.equals(image.getContentType(), IMAGE_AVIF.toString()))
                throw new NotTheCorrectImageFileException(image.getOriginalFilename() + " " + INCORRECT_IMAGE_FILE);

            if(image.getSize() > MAX_FILE_SIZE)
                throw new NotTheCorrectImageFileException(FILE_TOO_BIG);

            Path categoryFolder = Paths.get(CATEGORY_FOLDER + getGender(category.getGender()) + SEPARATOR + category.getType()).toAbsolutePath().normalize();

            if(!Files.exists(categoryFolder)){
                Files.createDirectories(categoryFolder);
                LOGGER.info(DIRECTORY_CREATED + "{}", categoryFolder);
            }

            Files.deleteIfExists(Paths.get(categoryFolder + category.getCategoryName().replace(" ", "_") + DOT + AVIF_EXTENSION));
            Files.copy(image.getInputStream(), categoryFolder.resolve(category.getCategoryName().replace(" ", "_") + DOT + AVIF_EXTENSION), REPLACE_EXISTING);

            category.setImgUrl(setTheImgUrl(category.getCategoryName(), category.getType(), category.getGender()));

            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + "{}", image.getOriginalFilename());
        }
    }

    private String setTheImgUrl(String categoryName, String categoryType, Character gender){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(CATEGORY_IMAGE_PATH + getGender(gender) + SEPARATOR + categoryType +
                SEPARATOR + categoryName.replace(" ", "_") + DOT + AVIF_EXTENSION).toUriString();
    }

    private String getGender(Character g){
        if(g.equals('M'))
            return "men";
        else
            return "women";
    }
}