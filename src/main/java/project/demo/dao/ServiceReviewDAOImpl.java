package project.demo.dao;

import project.demo.DataBase.DatabaseConfig;
import project.demo.models.ServiceReview;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceReviewDAOImpl implements ServiceReviewDAO {

    @Override
    public boolean addReview(ServiceReview review) {
        String query = "INSERT INTO service_reviews (service_id, user_id, booking_id, rating, review_text, created_at) VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, review.getServiceId());
            stmt.setInt(2, review.getUserId());
            if (review.getBookingId() != null) {
                stmt.setInt(3, review.getBookingId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, review.getRating());
            stmt.setString(5, review.getReviewText());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ServiceReview> getReviewsByServiceId(int serviceId) {
        List<ServiceReview> reviews = new ArrayList<>();
        String query = "SELECT sr.*, u.username FROM service_reviews sr JOIN users u ON sr.user_id = u.id WHERE sr.service_id = ? ORDER BY sr.created_at DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reviews.add(mapReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public List<ServiceReview> getReviewsByUserId(int userId) {
        List<ServiceReview> reviews = new ArrayList<>();
        String query = "SELECT sr.*, u.username FROM service_reviews sr JOIN users u ON sr.user_id = u.id WHERE sr.user_id = ? ORDER BY sr.created_at DESC";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reviews.add(mapReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public double getAverageRating(int serviceId) {
        String query = "SELECT COALESCE(AVG(rating), 0) FROM service_reviews WHERE service_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getReviewCount(int serviceId) {
        String query = "SELECT COUNT(*) FROM service_reviews WHERE service_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean hasUserReviewedService(int userId, int serviceId) {
        String query = "SELECT COUNT(*) FROM service_reviews WHERE user_id = ? AND service_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, serviceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private ServiceReview mapReview(ResultSet rs) throws SQLException {
        ServiceReview review = new ServiceReview();
        review.setId(rs.getInt("id"));
        review.setServiceId(rs.getInt("service_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setUsername(rs.getString("username"));
        review.setRating(rs.getInt("rating"));
        review.setReviewText(rs.getString("review_text"));
        int bookingId = rs.getInt("booking_id");
        if (!rs.wasNull()) review.setBookingId(bookingId);
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) review.setCreatedAt(ts.toLocalDateTime());
        return review;
    }
}
