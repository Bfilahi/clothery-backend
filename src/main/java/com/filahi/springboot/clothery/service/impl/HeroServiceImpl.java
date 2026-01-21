package com.filahi.springboot.clothery.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.filahi.springboot.clothery.entity.Hero;
import com.filahi.springboot.clothery.repository.HeroRepository;
import com.filahi.springboot.clothery.service.HeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;


@Service
@Transactional
public class HeroServiceImpl implements HeroService {
    private final Cloudinary cloudinary;
    private final HeroRepository heroRepository;

    @Autowired
    public HeroServiceImpl(Cloudinary cloudinary, HeroRepository HeroRepository){
        this.cloudinary = cloudinary;
        this.heroRepository = HeroRepository;
    }


    @Override
    public Hero addHeroImages(MultipartFile leftFile, MultipartFile rightFile) throws IOException {
        Hero hero = this.heroRepository.findAll().stream().findFirst().orElse(new Hero());

        if (leftFile != null && !leftFile.isEmpty()) {
            if (hero.getLeftPublicId() != null)
                this.cloudinary.uploader().destroy(hero.getLeftPublicId(), ObjectUtils.asMap("folder", "clothery"));

            Map<?, ?> result = uploadImage(leftFile);
            hero.setLeftImgUrl((String) result.get("url"));
            hero.setLeftPublicId((String) result.get("public_id"));
        }

        if (rightFile != null && !rightFile.isEmpty()) {
            if (hero.getRightPublicId() != null)
                this.cloudinary.uploader().destroy(hero.getRightPublicId(), ObjectUtils.asMap("folder", "clothery"));

            Map<?, ?> result = uploadImage(rightFile);
            hero.setRightImgUrl((String) result.get("url"));
            hero.setRightPublicId((String) result.get("public_id"));
        }
        this.heroRepository.save(hero);

        return hero;
    }

    @Override
    public Hero getHeroImages() {
        return this.heroRepository.findAll().stream().findFirst().orElseThrow(
                () -> new IllegalArgumentException("Hero not found.")
        );
    }

    private Map<?,?> uploadImage(MultipartFile file) throws IOException {
        try{
            return (Map<?,?>) this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "clothery"));
        }catch (IOException exception){
            throw new IOException("Failed to upload image to Cloudinary.", exception);
        }
    }
}
