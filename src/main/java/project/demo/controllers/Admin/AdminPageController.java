package project.demo.controllers.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import project.demo.DataBase.DatabaseConfig;
import project.demo.models.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminPageController {

    @FXML private Label totalUsersLabel;
    @FXML private Label totalOrdersLabel;
    @FXML private Label totalServicesLabel;
    @FXML private Label totalProductsLabel;
    @FXML private Label totalEmployeesLabel;
    @FXML private AnchorPane contentPane;

    @FXML
    public void initialize() {
        loadDashboardStats();
    }

    private void loadDashboardStats() {
        String[] queries = {
            "SELECT COUNT(*) FROM users",
            "SELECT COUNT(*) FROM orders",
            "SELECT COUNT(*) FROM service",
            "SELECT COUNT(*) FROM products",
            "SELECT COUNT(*) FROM employee"
        };
        Label[] labels = {totalUsersLabel, totalOrdersLabel, totalServicesLabel, totalProductsLabel, totalEmployeesLabel};

        for (int i = 0; i < queries.length; i++) {
            try (Connection conn = DatabaseConfig.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(queries[i])) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    labels[i].setText(String.valueOf(rs.getInt(1)));
                }
            } catch (Exception e) {
                labels[i].setText("0");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleManageUsers() {
        loadView("/project/demo/FXMLAdminPage/ManageUsers.fxml");
    }

    @FXML
    private void handleManageProducts() {
        loadView("/project/demo/FXMLAdminPage/ManageProducts.fxml");
    }

    @FXML
    private void handleManageServices() {
        loadView("/project/demo/FXMLAdminPage/ManageServices.fxml");
    }

    @FXML
    private void handleManageOrders() {
        loadView("/project/demo/FXMLAdminPage/ManageOrders.fxml");
    }

    @FXML
    private void handleManageEmployees() {
        loadView("/project/demo/FXMLAdminPage/ManageEmployees.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            AnchorPane view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
