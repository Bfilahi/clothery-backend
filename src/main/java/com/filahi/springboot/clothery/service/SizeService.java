package com.filahi.springboot.clothery.service;

import com.filahi.springboot.clothery.entity.Size;

import java.util.List;

public interface SizeService {
    List<Size> getSizes();
    Size addSize(String size);
    void deleteSize(long sizeId);
}
