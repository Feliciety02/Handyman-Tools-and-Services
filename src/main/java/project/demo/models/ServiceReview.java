package project.demo.models;

import java.time.LocalDateTime;

public class ServiceReview {
    private int id;
    private int serviceId;
    private int userId;
    private String username;
    private Integer bookingId;
    private int rating;
    private String reviewText;
    private LocalDateTime createdAt;

    public ServiceReview() {}

    public ServiceReview(int id, int serviceId, int userId, String username, Integer bookingId, int rating, String reviewText, LocalDateTime createdAt) {
        this.id = id;
        this.serviceId = serviceId;
        this.userId = userId;
        this.username = username;
        this.bookingId = bookingId;
        this.rating = rating;
        this.reviewText = reviewText;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getBookingId() { return bookingId; }
    public void setBookingId(Integer bookingId) { this.bookingId = bookingId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getReviewText() { return reviewText; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
