package project.demo.controllers.LoginSignup;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.demo.DataBase.DatabaseConfig;
import project.demo.utils.PasswordUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label warningLabel;

    @FXML
    private Label successLabel;

    private boolean emailVerified = false;

    @FXML
    private void handleVerifyEmail() {
        warningLabel.setVisible(false);
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            showWarning("Please enter your email address.");
            return;
        }

        String query = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                emailVerified = true;
                showSuccess("Email verified! You can now reset your password.");
                newPasswordField.setDisable(false);
                confirmPasswordField.setDisable(false);
            } else {
                showWarning("No account found with that email address.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showWarning("Database error. Please try again.");
        }
    }

    @FXML
    private void handleResetPassword() {
        if (!emailVerified) {
            showWarning("Please verify your email first.");
            return;
        }

        String email = emailField.getText().trim();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showWarning("All fields are required.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showWarning("Passwords do not match.");
            return;
        }

        if (newPassword.length() < 8) {
            showWarning("Password must be at least 8 characters.");
            return;
        }

        String hashedPassword = PasswordUtils.hashPassword(newPassword);
        String updateQuery = "UPDATE users SET password = ? WHERE email = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, hashedPassword);
            stmt.setString(2, email);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                showSuccess("Password reset successfully! You can now login.");
                navigateToLogin();
            } else {
                showWarning("Failed to reset password. Try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showWarning("Database error. Please try again.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        navigateToLogin();
    }

    private void navigateToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/project/demo/FXMLLoginSignup/LogInPage.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showWarning(String message) {
        warningLabel.setText(message);
        warningLabel.setVisible(true);
        successLabel.setVisible(false);
    }

    private void showSuccess(String message) {
        successLabel.setText(message);
        successLabel.setVisible(true);
        warningLabel.setVisible(false);
    }
}
