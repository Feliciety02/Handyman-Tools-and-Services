package project.demo.controllers.Profile;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.util.Duration;
import project.demo.DataBase.DatabaseConfig;
import project.demo.models.UserSession;
import project.demo.utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChangePasswordController {

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label warningLabel;

    @FXML
    private Label successLabel;

    @FXML
    private void handleChangePassword() {
        warningLabel.setVisible(false);
        successLabel.setVisible(false);

        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showWarning("All fields are required.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showWarning("New passwords do not match.");
            return;
        }

        if (newPassword.length() < 8) {
            showWarning("New password must be at least 8 characters.");
            return;
        }

        UserSession session = UserSession.getInstance();
        int userId = session.getUserId();

        String query = "SELECT password FROM users WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                showWarning("User not found.");
                return;
            }

            String storedHash = rs.getString("password");
            if (!PasswordUtils.checkPassword(currentPassword, storedHash)) {
                showWarning("Current password is incorrect.");
                return;
            }

            String newHash = PasswordUtils.hashPassword(newPassword);
            String updateQuery = "UPDATE users SET password = ? WHERE id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, newHash);
                updateStmt.setInt(2, userId);
                updateStmt.executeUpdate();
            }

            showSuccess("Password changed successfully!");
            currentPasswordField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();

        } catch (Exception e) {
            e.printStackTrace();
            showWarning("An error occurred. Please try again.");
        }
    }

    private void showWarning(String message) {
        warningLabel.setText(message);
        warningLabel.setVisible(true);
        FadeTransition fade = new FadeTransition(Duration.seconds(3), warningLabel);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.play();
    }

    private void showSuccess(String message) {
        successLabel.setText(message);
        successLabel.setVisible(true);
        FadeTransition fade = new FadeTransition(Duration.seconds(3), successLabel);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.play();
    }
}
