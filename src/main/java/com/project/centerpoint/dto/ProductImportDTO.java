package com.project.centerpoint.dto;

import com.project.centerpoint.entity.ProductType;
import lombok.Data;

@Data
public class ProductImportDTO {
    private String name;
    private Long brandId;
    private Long categoryId;
    private int price;
    private int discountPercent;
    private int stock;
    private String description;
    private ProductType type;
    
    // Specs
    private String cpu;
    private String ram;
    private String storage;
    private String screen;
    private String vga;
    private String os;
    private String weight;
    private java.util.List<String> imageUrls;
}
