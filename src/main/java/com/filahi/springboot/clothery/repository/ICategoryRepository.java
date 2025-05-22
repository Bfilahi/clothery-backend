package com.filahi.springboot.clothery.repository;

import com.filahi.springboot.clothery.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;


@CrossOrigin("http://localhost:4200")
public interface ICategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c where c.type = :type and c.gender = :gender")
    Category findByTypeAndGender(@Param("type") String type, @Param("gender") Character gender);
}
