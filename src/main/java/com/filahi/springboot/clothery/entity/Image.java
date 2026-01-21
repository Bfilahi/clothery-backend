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
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "public_id", nullable = false)
    private String publicId;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    @Override
    public String toString() {
        return "Image{" +
                "imgUrl='" + imgUrl + '\'' +
                ", id=" + id +
                '}';
    }
}
