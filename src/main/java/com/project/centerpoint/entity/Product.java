package com.project.centerpoint.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private int price;

    @Column(name = "discount_percent")
    @Builder.Default
    private Integer discountPercent = 0;

    public Integer getDiscountPercent() {
        return discountPercent == null ? 0 : discountPercent;
    }

    @Column(nullable = false)
    private int stock;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "product_type")
    @Builder.Default
    private ProductType type = ProductType.OFFICE;

    private String cpu;
    private String ram;
    private String storage;
    private String screen;
    private String vga;
    private String os;
    private String weight;

    @Column(name = "specs_json", columnDefinition = "JSON")
    private String specsJson;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<ProductImage> images;

    @Column(name = "avg_rating")
    @Builder.Default
    private Double avgRating = 0.0;

    @Column(name = "review_count")
    @Builder.Default
    private Integer reviewCount = 0;

    public Double getAvgRating() {
        return avgRating == null ? 0.0 : avgRating;
    }

    public Integer getReviewCount() {
        return reviewCount == null ? 0 : reviewCount;
    }

    public String getPrimaryImageUrl() {
        if (images != null && !images.isEmpty()) {
            for (ProductImage img : images) {
                if (img.isPrimary()) {
                    return "/uploads/products/" + img.getImageUrl();
                }
            }
            return "/uploads/products/" + images.get(0).getImageUrl();
        }
        return null;
    }

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
