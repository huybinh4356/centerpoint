package com.project.centerpoint.service;

import com.project.centerpoint.entity.Promotion;
import com.project.centerpoint.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id).orElse(null);
    }

    public Promotion getPromotionByCode(String code) {
        return promotionRepository.findByCodeAndIsActiveTrue(code).orElse(null);
    }

    @Transactional
    public Promotion savePromotion(Promotion promotion) {
        if (promotion.getCode() != null) {
            promotion.setCode(promotion.getCode().toUpperCase());
        }
        return promotionRepository.save(promotion);
    }

    @Transactional
    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    public Promotion validateCode(String code, int orderTotal) {
        Promotion promotion = getPromotionByCode(code);
        if (promotion == null) {
            throw new RuntimeException("Mã giảm giá không tồn tại hoặc đã bị vô hiệu hóa.");
        }
        
        LocalDateTime now = LocalDateTime.now();
        if (promotion.getStartDate() != null && now.isBefore(promotion.getStartDate())) {
            throw new RuntimeException("Mã giảm giá chưa đến thời gian áp dụng.");
        }
        if (promotion.getEndDate() != null && now.isAfter(promotion.getEndDate())) {
            throw new RuntimeException("Mã giảm giá đã hết hạn.");
        }
        if (promotion.getUsageLimit() != null && promotion.getUsageLimit() > 0 && promotion.getUsedCount() >= promotion.getUsageLimit()) {
            throw new RuntimeException("Mã giảm giá đã hết lượt sử dụng.");
        }
        if (promotion.getMinOrderValue() != null && orderTotal < promotion.getMinOrderValue()) {
            throw new RuntimeException("Đơn hàng chưa đạt giá trị tối thiểu " + promotion.getMinOrderValue() + "đ để sử dụng mã này.");
        }
        
        return promotion;
    }

    public int calculateDiscount(Promotion promotion, int orderTotal) {
        if (promotion == null) return 0;
        int discount = 0;
        
        if ("PERCENT".equalsIgnoreCase(promotion.getDiscountType())) {
            if (promotion.getDiscountPercent() != null) {
                discount = (orderTotal * promotion.getDiscountPercent()) / 100;
                if (promotion.getMaxDiscount() != null && promotion.getMaxDiscount() > 0 && discount > promotion.getMaxDiscount()) {
                    discount = promotion.getMaxDiscount();
                }
            }
        } else if ("AMOUNT".equalsIgnoreCase(promotion.getDiscountType())) {
            if (promotion.getDiscountAmount() != null) {
                discount = promotion.getDiscountAmount();
            }
        }
        
        // Không thể giảm quá số tiền đơn hàng
        if (discount > orderTotal) {
            discount = orderTotal;
        }
        
        return discount;
    }
}
