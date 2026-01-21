package com.filahi.springboot.clothery.repository;

import com.filahi.springboot.clothery.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
