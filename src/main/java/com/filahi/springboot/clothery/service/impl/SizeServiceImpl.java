package com.filahi.springboot.clothery.service.impl;

import com.filahi.springboot.clothery.entity.Size;
import com.filahi.springboot.clothery.repository.SizeRepository;
import com.filahi.springboot.clothery.service.SizeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;

    public SizeServiceImpl(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }
    @Override
    public List<Size> getSizes() {
        return this.sizeRepository.findAll();
    }

    @Override
    public Size addSize(String size) {
        Size newSize = new Size(0, size);
        this.sizeRepository.save(newSize);
        return newSize;
    }

    @Override
    public void deleteSize(long sizeId) {
        this.sizeRepository.deleteById(sizeId);
    }
}
