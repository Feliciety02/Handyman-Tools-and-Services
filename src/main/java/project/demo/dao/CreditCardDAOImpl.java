package project.demo.dao;

import project.demo.DataBase.DatabaseConfig;
import project.demo.models.CreditCard;
import project.demo.utils.EncryptionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreditCardDAOImpl implements CreditCardDAO {
    private final DatabaseConfig dbConfig = new DatabaseConfig();

    @Override
    public boolean saveOrUpdateCreditCardAccount(CreditCard creditCard) {
        String selectQuery = "SELECT id FROM creditcard WHERE user_id = ?";
        String updateQuery = "UPDATE creditcard SET card_name = ?, card_number = ?, cvv = ?, billing_address = ?, zip_code = ?, expiry = ? WHERE user_id = ?";
        String insertQuery = "INSERT INTO creditcard (user_id, card_name, card_number, cvv, billing_address, zip_code, expiry) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {

            selectStatement.setInt(1, creditCard.getUserId());
            ResultSet resultSet = selectStatement.executeQuery();

            String encryptedCardNumber = EncryptionUtils.encrypt(creditCard.getCardNumber());
            String encryptedCvv = EncryptionUtils.encrypt(creditCard.getCvv());

            if (resultSet.next()) {
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setString(1, creditCard.getCardName());
                    updateStatement.setString(2, encryptedCardNumber);
                    updateStatement.setString(3, encryptedCvv);
                    updateStatement.setString(4, creditCard.getBillingAddress());
                    updateStatement.setString(5, creditCard.getZipCode());
                    updateStatement.setString(6, creditCard.getExpiry());
                    updateStatement.setInt(7, creditCard.getUserId());
                    updateStatement.executeUpdate();
                    return true;
                }
            } else {
                try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                    insertStatement.setInt(1, creditCard.getUserId());
                    insertStatement.setString(2, creditCard.getCardName());
                    insertStatement.setString(3, encryptedCardNumber);
                    insertStatement.setString(4, encryptedCvv);
                    insertStatement.setString(5, creditCard.getBillingAddress());
                    insertStatement.setString(6, creditCard.getZipCode());
                    insertStatement.setString(7, creditCard.getExpiry());
                    insertStatement.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CreditCard getCreditCardByUserId(int userId) {
        String query = "SELECT * FROM creditcard WHERE user_id = ?";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                CreditCard card = new CreditCard(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getString("card_name"),
                        EncryptionUtils.decrypt(resultSet.getString("card_number")),
                        EncryptionUtils.decrypt(resultSet.getString("cvv")),
                        resultSet.getString("expiry"),
                        resultSet.getString("billing_address"),
                        resultSet.getString("zip_code")
                );
                return card;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
