package project.demo.dao;

import project.demo.models.ServiceReview;
import java.util.List;

public interface ServiceReviewDAO {
    boolean addReview(ServiceReview review);
    List<ServiceReview> getReviewsByServiceId(int serviceId);
    List<ServiceReview> getReviewsByUserId(int userId);
    double getAverageRating(int serviceId);
    int getReviewCount(int serviceId);
    boolean hasUserReviewedService(int userId, int serviceId);
}
