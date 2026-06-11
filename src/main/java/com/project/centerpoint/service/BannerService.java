package com.project.centerpoint.service;

import com.project.centerpoint.entity.Banner;
import com.project.centerpoint.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;
    private final FileStorageService fileStorageService;

    public List<Banner> getAllBanners() {
        return bannerRepository.findAll(Sort.by(Sort.Direction.ASC, "displayOrder"));
    }

    public List<Banner> getActiveMainBanners() {
        return bannerRepository.findByIsActiveTrueAndTypeOrderByDisplayOrderAsc("MAIN");
    }

    public List<Banner> getActiveSubBanners() {
        return bannerRepository.findByIsActiveTrueAndTypeOrderByDisplayOrderAsc("SUB");
    }

    public Banner getBannerById(Long id) {
        return bannerRepository.findById(id).orElse(null);
    }

    @Transactional
    public Banner saveBanner(Banner banner, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            banner.setImageUrl(fileName);
        }
        // If it's a new banner and no order is set, put it at the end
        if (banner.getId() == null && banner.getDisplayOrder() == 0) {
            long count = bannerRepository.count();
            banner.setDisplayOrder((int) count + 1);
        }
        return bannerRepository.save(banner);
    }

    @Transactional
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }
}
