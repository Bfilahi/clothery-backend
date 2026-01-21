package com.filahi.springboot.clothery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Table(name = "sizes")
@Getter
@Setter
@ToString
public class Size {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;

    @Column(name = "size", nullable = false)
    private String size;

    @ManyToMany(mappedBy = "sizes")
    @JsonIgnore
    private List<Product> products;

    public Size(){}

    public Size(long id, String size) {
        this.id = id;
        this.size = size;
    }

}
