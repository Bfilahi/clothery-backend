package com.filahi.springboot.clothery.service;

import com.filahi.springboot.clothery.entity.Hero;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IHeroService {
    Hero addHeroImages(MultipartFile leftImage, MultipartFile rightImage) throws IOException;
}
