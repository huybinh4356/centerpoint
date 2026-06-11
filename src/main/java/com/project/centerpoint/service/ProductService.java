package com.project.centerpoint.service;

import com.project.centerpoint.entity.Product;
import com.project.centerpoint.entity.ProductImage;
import com.project.centerpoint.repository.ProductImageRepository;
import com.project.centerpoint.repository.ProductRepository;
import com.project.centerpoint.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.centerpoint.dto.ProductImportDTO;
import com.project.centerpoint.entity.Brand;
import com.project.centerpoint.entity.Category;
import com.project.centerpoint.repository.BrandRepository;
import com.project.centerpoint.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final FileStorageService fileStorageService;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getLatestProducts() {
        return productRepository.findTop8ByOrderByCreatedAtDesc();
    }

    public org.springframework.data.domain.Page<Product> searchProducts(String keyword, Long categoryId, Long brandId, com.project.centerpoint.entity.ProductType type, int page, int size, String sort) {
        org.springframework.data.domain.Sort sortObj = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt");
        if ("price_asc".equals(sort)) {
            sortObj = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "price");
        } else if ("price_desc".equals(sort)) {
            sortObj = org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "price");
        }
        
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size, sortObj);
        return productRepository.searchProducts(keyword, categoryId, brandId, type, pageable);
    }

    public Product getProductBySlug(String slug) {
        return productRepository.findBySlug(slug).orElse(null);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public Product saveProduct(Product product, MultipartFile[] files, int primaryImageIndex) throws IOException {
        if (product.getSlug() == null || product.getSlug().isEmpty()) {
            product.setSlug(SlugUtil.toSlug(product.getName()));
        }
        if (product.getSpecsJson() != null && product.getSpecsJson().trim().isEmpty()) {
            product.setSpecsJson(null);
        }
        Product savedProduct = productRepository.save(product);

        if (files != null && files.length > 0 && !files[0].isEmpty()) {
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                if (!file.isEmpty()) {
                    String fileName = fileStorageService.storeFile(file);
                    ProductImage productImage = new ProductImage();
                    productImage.setProduct(savedProduct);
                    productImage.setImageUrl(fileName);
                    productImage.setPrimary(i == primaryImageIndex);
                    productImageRepository.save(productImage);
                }
            }
        }
        return savedProduct;
    }

    @Transactional
    public void deleteProduct(Long id) {
        productImageRepository.deleteByProductId(id);
        productRepository.deleteById(id);
    }

    @Transactional
    public void importProductsFromJson(MultipartFile file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = file.getInputStream()) {
            List<ProductImportDTO> importDTOs = mapper.readValue(inputStream, new TypeReference<List<ProductImportDTO>>() {});
            
            for (ProductImportDTO dto : importDTOs) {
                Product product = new Product();
                product.setName(dto.getName());
                product.setSlug(SlugUtil.toSlug(dto.getName()));
                product.setPrice(dto.getPrice());
                product.setDiscountPercent(dto.getDiscountPercent());
                product.setStock(dto.getStock());
                product.setDescription(dto.getDescription());
                product.setType(dto.getType() != null ? dto.getType() : com.project.centerpoint.entity.ProductType.OFFICE);
                product.setCpu(dto.getCpu());
                product.setRam(dto.getRam());
                product.setStorage(dto.getStorage());
                product.setScreen(dto.getScreen());
                product.setVga(dto.getVga());
                product.setOs(dto.getOs());
                product.setWeight(dto.getWeight());
                
                Brand brand = brandRepository.findById(dto.getBrandId())
                        .orElseThrow(() -> new RuntimeException("Brand not found for ID: " + dto.getBrandId()));
                product.setBrand(brand);
                
                Category category = categoryRepository.findById(dto.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found for ID: " + dto.getCategoryId()));
                product.setCategory(category);
                
                Product savedProduct = productRepository.save(product);

                if (dto.getImageUrls() != null && !dto.getImageUrls().isEmpty()) {
                    for (int i = 0; i < dto.getImageUrls().size(); i++) {
                        ProductImage productImage = new ProductImage();
                        productImage.setProduct(savedProduct);
                        productImage.setImageUrl(dto.getImageUrls().get(i));
                        productImage.setPrimary(i == 0); // First image is primary
                        productImageRepository.save(productImage);
                    }
                }
            }
        }
    }
}
