package com.project.centerpoint.service;

import com.project.centerpoint.entity.Product;
import com.project.centerpoint.repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompareService {

    private final ProductRepository productRepository;
    private static final String COMPARE_SESSION_KEY = "COMPARE_PRODUCT_IDS";
    private static final int MAX_COMPARE_ITEMS = 3;

    @SuppressWarnings("unchecked")
    private List<Long> getCompareListFromSession(HttpSession session) {
        List<Long> compareList = (List<Long>) session.getAttribute(COMPARE_SESSION_KEY);
        if (compareList == null) {
            compareList = new ArrayList<>();
            session.setAttribute(COMPARE_SESSION_KEY, compareList);
        }
        return compareList;
    }

    public boolean addProductToCompare(Long productId, HttpSession session) {
        List<Long> compareList = getCompareListFromSession(session);
        if (compareList.contains(productId)) {
            return false; // Already exists
        }
        if (compareList.size() >= MAX_COMPARE_ITEMS) {
            return false; // Max reached
        }
        compareList.add(productId);
        session.setAttribute(COMPARE_SESSION_KEY, compareList);
        return true;
    }

    public void removeProductFromCompare(Long productId, HttpSession session) {
        List<Long> compareList = getCompareListFromSession(session);
        compareList.remove(productId);
        session.setAttribute(COMPARE_SESSION_KEY, compareList);
    }

    public void clearCompare(HttpSession session) {
        session.removeAttribute(COMPARE_SESSION_KEY);
    }

    public List<Product> getCompareProducts(HttpSession session) {
        List<Long> compareList = getCompareListFromSession(session);
        if (compareList.isEmpty()) {
            return new ArrayList<>();
        }
        return productRepository.findAllById(compareList);
    }
    
    public int getCompareCount(HttpSession session) {
        return getCompareListFromSession(session).size();
    }
}
