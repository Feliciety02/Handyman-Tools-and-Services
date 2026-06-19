package project.demo.dao;

import project.demo.models.ProductReview;
import java.util.List;

public interface ProductReviewDAO {
    boolean addReview(ProductReview review);
    List<ProductReview> getReviewsByProductId(int productId);
    List<ProductReview> getReviewsByUserId(int userId);
    double getAverageRating(int productId);
    int getReviewCount(int productId);
    boolean hasUserReviewedProduct(int userId, int productId);
}
