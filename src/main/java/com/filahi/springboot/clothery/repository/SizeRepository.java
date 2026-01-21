package com.filahi.springboot.clothery.repository;

import com.filahi.springboot.clothery.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

}
