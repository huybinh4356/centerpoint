package com.project.centerpoint.service;

import com.project.centerpoint.entity.Brand;
import com.project.centerpoint.repository.BrandRepository;
import com.project.centerpoint.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Brand saveBrand(Brand brand) {
        if (brand.getSlug() == null || brand.getSlug().isEmpty()) {
            brand.setSlug(SlugUtil.toSlug(brand.getName()));
        }
        return brandRepository.save(brand);
    }

    public Brand getBrandById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
    }
}
