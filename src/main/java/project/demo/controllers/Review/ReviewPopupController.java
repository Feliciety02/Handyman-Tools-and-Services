package project.demo.controllers.Review;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import project.demo.DataBase.DatabaseConfig;
import project.demo.models.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReviewPopupController {

    @FXML private Label titleLabel;
    @FXML private Label errorLabel;
    @FXML private TextArea reviewTextArea;

    private int entityId;
    private String entityType; // "product" or "service"

    private final int[] rating = {5};

    public void setEntity(int entityId, String entityType, String entityName) {
        this.entityId = entityId;
        this.entityType = entityType;
        titleLabel.setText("Review: " + entityName);
    }

    @FXML
    private void setRating1() { rating[0] = 1; }
    @FXML
    private void setRating2() { rating[0] = 2; }
    @FXML
    private void setRating3() { rating[0] = 3; }
    @FXML
    private void setRating4() { rating[0] = 4; }
    @FXML
    private void setRating5() { rating[0] = 5; }

    @FXML
    private void handleSubmitReview() {
        String text = reviewTextArea.getText().trim();
        if (text.isEmpty()) {
            errorLabel.setText("Please write a review.");
            errorLabel.setVisible(true);
            return;
        }

        int userId = UserSession.getInstance().getUserId();
        String table = entityType.equals("product") ? "product_reviews" : "service_reviews";
        String idColumn = entityType.equals("product") ? "product_id" : "service_id";
        String query = "INSERT INTO " + table + " (" + idColumn + ", user_id, rating, review_text, created_at) VALUES (?, ?, ?, ?, NOW())";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, entityId);
            stmt.setInt(2, userId);
            stmt.setInt(3, rating[0]);
            stmt.setString(4, text);
            stmt.executeUpdate();

            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Review submitted! Thank you.");
            errorLabel.setVisible(true);

            Stage stage = (Stage) titleLabel.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Failed to submit review.");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) titleLabel.getScene().getWindow();
        stage.close();
    }
}
