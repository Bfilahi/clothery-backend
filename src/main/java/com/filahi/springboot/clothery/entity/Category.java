package com.filahi.springboot.clothery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String categoryName;

    @Column(name = "type")
    private String type;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "public_id")
    private String publicId;

    @Column(name = "gender")
    private Character gender;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Product> products;


    @Override
    public String toString() {
        return "Category{" +
                "gender=" + gender +
                ", imgUrl='" + imgUrl + '\'' +
                ", type='" + type + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", id=" + id +
                '}';
    }
}