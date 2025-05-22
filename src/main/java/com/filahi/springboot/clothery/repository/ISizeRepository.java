package com.filahi.springboot.clothery.repository;

import com.filahi.springboot.clothery.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ISizeRepository extends JpaRepository<Size, Long> {

    @Modifying
    @Query("DELETE FROM Size s WHERE s.product.id = :id")
    void deleteByProductId(@Param("id") Long id);
}
