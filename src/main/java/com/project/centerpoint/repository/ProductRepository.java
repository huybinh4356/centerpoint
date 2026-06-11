package com.project.centerpoint.repository;

import com.project.centerpoint.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlug(String slug);
    java.util.List<Product> findTop8ByOrderByCreatedAtDesc();
    
    @org.springframework.data.jpa.repository.Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
            "(:brandId IS NULL OR p.brand.id = :brandId) AND " +
            "(:type IS NULL OR p.type = :type)")
    org.springframework.data.domain.Page<Product> searchProducts(
            @org.springframework.data.repository.query.Param("keyword") String keyword,
            @org.springframework.data.repository.query.Param("categoryId") Long categoryId,
            @org.springframework.data.repository.query.Param("brandId") Long brandId,
            @org.springframework.data.repository.query.Param("type") com.project.centerpoint.entity.ProductType type,
            org.springframework.data.domain.Pageable pageable);
}
