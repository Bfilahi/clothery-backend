package com.filahi.springboot.clothery.service.impl;

import com.filahi.springboot.clothery.entity.Hero;
import com.filahi.springboot.clothery.exception.domain.NotTheCorrectImageFileException;
import com.filahi.springboot.clothery.repository.IHeroRepository;
import com.filahi.springboot.clothery.service.IHeroService;
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
@Transactional
public class HeroServiceImpl implements IHeroService {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final MediaType IMAGE_AVIF = new MediaType("image", "avif");

    private final IHeroRepository IHeroRepository;

    @Autowired
    public HeroServiceImpl(IHeroRepository IHeroRepository){
        this.IHeroRepository = IHeroRepository;
    }


    @Override
    public Hero addHeroImages(MultipartFile leftImage, MultipartFile rightImage) throws IOException {
        Hero hero = new Hero();

        System.out.println(leftImage.getName());
        System.out.println(rightImage.getName());

        hero.setLeftImage(saveImage(hero, leftImage));
        LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + "{}", leftImage.getOriginalFilename());
        hero.setRightImage(saveImage(hero, rightImage));
        LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + "{}", rightImage.getOriginalFilename());

        this.IHeroRepository.save(hero);

        return hero;
    }


    private String saveImage(Hero hero, MultipartFile image) throws IOException {
        if(image != null){
            if(!Objects.equals(image.getContentType(), IMAGE_AVIF.toString()))
                throw new NotTheCorrectImageFileException(image.getOriginalFilename() + " " + INCORRECT_IMAGE_FILE);

            if(image.getSize() > MAX_FILE_SIZE)
                throw new NotTheCorrectImageFileException(FILE_TOO_BIG);

            Path heroFolder = Paths.get(HERO_FOLDER).toAbsolutePath().normalize();

            if(!Files.exists(heroFolder)){
                Files.createDirectories(heroFolder);
                LOGGER.info(DIRECTORY_CREATED + "{}", heroFolder);
            }

            Files.deleteIfExists(Paths.get(heroFolder + image.getName().replace(" ", "_") + DOT + AVIF_EXTENSION));
            Files.copy(image.getInputStream(), heroFolder.resolve(image.getName().replace(" ", "_") + DOT + AVIF_EXTENSION), REPLACE_EXISTING);

            return ServletUriComponentsBuilder.fromCurrentContextPath().path(HERO_IMAGE_PATH + SEPARATOR + image.getName().replace(" ", "_") +
                    DOT + AVIF_EXTENSION).toUriString();

//            hero.setLeftImage(leftImgUrl);

//            LOGGER.info(FILE_SAVED_IN_FILE_SYSTEM + "{}", image.getOriginalFilename());

        }

        return "";
    }
}
