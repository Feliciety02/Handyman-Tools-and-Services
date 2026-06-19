package project.demo.controllers.Review;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import project.demo.dao.ProductReviewDAO;
import project.demo.dao.ProductReviewDAOImpl;
import project.demo.dao.ServiceReviewDAO;
import project.demo.dao.ServiceReviewDAOImpl;
import project.demo.models.ProductReview;
import project.demo.models.ServiceReview;

import java.util.List;

public class ReviewListController {

    @FXML private Label titleLabel;
    @FXML private Label avgRatingLabel;
    @FXML private VBox reviewsContainer;

    private final ProductReviewDAO productReviewDAO = new ProductReviewDAOImpl();
    private final ServiceReviewDAO serviceReviewDAO = new ServiceReviewDAOImpl();

    public void setEntity(int entityId, String entityType, String entityName) {
        titleLabel.setText("Reviews: " + entityName);

        if ("product".equals(entityType)) {
            double avg = productReviewDAO.getAverageRating(entityId);
            int count = productReviewDAO.getReviewCount(entityId);
            avgRatingLabel.setText(String.format("\u2605 %.1f (%d reviews)", avg, count));

            List<ProductReview> reviews = productReviewDAO.getReviewsByProductId(entityId);
            for (ProductReview review : reviews) {
                addReviewCard(review.getUsername(), review.getRating(), review.getReviewText());
            }
        } else {
            double avg = serviceReviewDAO.getAverageRating(entityId);
            int count = serviceReviewDAO.getReviewCount(entityId);
            avgRatingLabel.setText(String.format("\u2605 %.1f (%d reviews)", avg, count));

            List<ServiceReview> reviews = serviceReviewDAO.getReviewsByServiceId(entityId);
            for (ServiceReview review : reviews) {
                addReviewCard(review.getUsername(), review.getRating(), review.getReviewText());
            }
        }
    }

    private void addReviewCard(String username, int rating, String text) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/demo/FXMLReviewPage/ReviewCard.fxml"));
            VBox card = loader.load();

            Label userLabel = (Label) card.lookup("#userLabel");
            Label ratingLabel = (Label) card.lookup("#ratingLabel");
            Label textLabel = (Label) card.lookup("#textLabel");

            if (userLabel != null) userLabel.setText(username);
            if (ratingLabel != null) ratingLabel.setText("\u2605 ".repeat(rating) + "\u2606 ".repeat(5 - rating));
            if (textLabel != null) textLabel.setText(text);

            reviewsContainer.getChildren().add(card);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
