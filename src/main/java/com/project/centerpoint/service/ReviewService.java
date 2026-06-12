package com.project.centerpoint.service;

import com.project.centerpoint.entity.OrderItem;
import com.project.centerpoint.entity.Product;
import com.project.centerpoint.entity.Review;
import com.project.centerpoint.entity.User;
import com.project.centerpoint.repository.OrderItemRepository;
import com.project.centerpoint.repository.ProductRepository;
import com.project.centerpoint.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProductOrderByCreatedAtDesc(product);
    }

    @Transactional
    public Review saveReview(User user, Long orderItemId, int rating, String comment) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> new RuntimeException("Item không tồn tại"));
        
        // Kiểm tra xem user có phải chủ đơn hàng không
        if (!orderItem.getOrder().getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền đánh giá sản phẩm này.");
        }
        
        // Kiểm tra đơn hàng đã giao chưa
        if (!"DELIVERED".equals(orderItem.getOrder().getStatus().name())) {
            throw new RuntimeException("Chỉ có thể đánh giá sau khi đã nhận hàng thành công.");
        }
        
        // Kiểm tra xem đã review chưa
        if (orderItem.getReview() != null) {
            throw new RuntimeException("Sản phẩm này đã được đánh giá.");
        }
        
        Product product = orderItem.getProduct();
        
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setOrderItem(orderItem);
        review.setRating(rating);
        review.setComment(comment);
        
        Review savedReview = reviewRepository.save(review);
        
        // Cập nhật Product
        Double avg = reviewRepository.getAverageRatingByProductId(product.getId());
        Integer count = reviewRepository.countReviewsByProductId(product.getId());
        
        product.setAvgRating(avg != null ? avg : rating);
        product.setReviewCount(count != null ? count : 1);
        productRepository.save(product);
        
        return savedReview;
    }

    @Transactional
    public Review updateReview(User user, Long reviewId, int newRating, String newComment) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Đánh giá không tồn tại."));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền chỉnh sửa đánh giá này.");
        }

        review.setRating(newRating);
        review.setComment(newComment);
        Review updatedReview = reviewRepository.save(review);

        // Cập nhật lại số sao trung bình của Product
        Product product = review.getProduct();
        Double avg = reviewRepository.getAverageRatingByProductId(product.getId());
        product.setAvgRating(avg != null ? avg : newRating);
        productRepository.save(product);

        return updatedReview;
    }
}
