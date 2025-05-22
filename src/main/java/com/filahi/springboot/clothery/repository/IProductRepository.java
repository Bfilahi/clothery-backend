package com.filahi.springboot.clothery.repository;

import com.filahi.springboot.clothery.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;


@CrossOrigin("http://localhost:4200")
public interface IProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.productName = :name AND p.description = :description AND p.gender = :gender")
    Product find(@Param("name") String name, @Param("description") String description, @Param("gender") Character gender);

    List<Product> findByGender(Character gender);

    Product findProductById(Long id);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
    List<Product> findProductsByCategory(@Param("id") Long id);

    Optional<Product> findByProductName(String productName);
}
