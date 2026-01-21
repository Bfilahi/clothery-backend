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
    private long id;

    private String leftImgUrl;
    private String leftPublicId;

    private String rightImgUrl;
    private String rightPublicId;

}
