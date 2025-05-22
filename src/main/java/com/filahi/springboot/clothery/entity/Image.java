package com.filahi.springboot.clothery.entity;


import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "images")
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "img_url")
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product_img;

    @Override
    public String toString() {
        return "Image{" +
                "imgUrl='" + imgUrl + '\'' +
                ", id=" + id +
                '}';
    }
}
