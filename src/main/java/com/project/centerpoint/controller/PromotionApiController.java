package com.project.centerpoint.controller;

import com.project.centerpoint.entity.Promotion;
import com.project.centerpoint.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
public class PromotionApiController {

    private final PromotionService promotionService;

    @GetMapping("/validate")
    public ResponseEntity<?> validateCode(@RequestParam String code, @RequestParam int orderTotal) {
        try {
            Promotion promotion = promotionService.validateCode(code.toUpperCase(), orderTotal);
            int discountAmount = promotionService.calculateDiscount(promotion, orderTotal);
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", true);
            response.put("code", promotion.getCode());
            response.put("discountAmount", discountAmount);
            response.put("newTotal", orderTotal - discountAmount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("valid", false);
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
