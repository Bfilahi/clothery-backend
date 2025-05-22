package com.filahi.springboot.clothery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hero")
@Getter
@Setter
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "left_image")
    public String leftImage;

    @Column(name = "right_image")
    public String rightImage;
}
