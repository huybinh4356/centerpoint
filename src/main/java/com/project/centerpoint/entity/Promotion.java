package com.project.centerpoint.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(name = "discount_type", nullable = false)
    @Builder.Default
    private String discountType = "PERCENT"; // "PERCENT" or "AMOUNT"

    @Column(name = "discount_percent")
    private Integer discountPercent;

    @Column(name = "discount_amount")
    private Integer discountAmount;

    @Column(name = "max_discount")
    private Integer maxDiscount;

    @Column(name = "min_order_value")
    private Integer minOrderValue;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "used_count")
    @Builder.Default
    private int usedCount = 0;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;
}
