package com.project.centerpoint.entity;

public enum ProductType {
    GAMING("Laptop Gaming"),
    OFFICE("Laptop Văn phòng"),
    WORKSTATION("Máy trạm (Workstation)"),
    MACBOOK("Macbook"),
    ACCESSORY("Phụ kiện");

    private final String displayName;

    ProductType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
