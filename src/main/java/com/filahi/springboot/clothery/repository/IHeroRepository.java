package com.filahi.springboot.clothery.repository;

import com.filahi.springboot.clothery.entity.Hero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IHeroRepository extends JpaRepository<Hero, Long> {
}
