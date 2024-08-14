package logic.engine.user.registration;

import newsGuardServer.DatabaseConfig;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class UserRegistrationDetails {
    private String firstName;
    private String lastName;
    private String country;
    private String email;
    private String password;
    private String imageURL;
    private String phoneNumber;
    private boolean locationAccessPermission;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;


    public UserRegistrationDetails(String firstName, String lastName, String country, String email, String password, String imageURL, String phoneNumber, boolean locationAccessPermission) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.email = email;
        this.password = password;
        this.imageURL = imageURL;
        this.phoneNumber = phoneNumber;
        this.locationAccessPermission = locationAccessPermission;
    }

    public String getEmail() {
        return email;
    }

    public boolean checkUserPassword(String passwordToCheck){
        return Objects.equals(passwordToCheck, this.password);
    }
    public void pushRegistrationDetailsToDB(int user_id)
    {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, country = ?, email = ?, phone_number = ?, password = ?, imageurl = ?, location_access_permission = ? WHERE user_id = ?";

        // Establish a database connection
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(),DB_CONFIG.getUsername(),DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the parameters
            preparedStatement.setString(1,firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, country);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, phoneNumber);
            preparedStatement.setString(6, password);
            preparedStatement.setString(7, imageURL);
            preparedStatement.setBoolean(8, locationAccessPermission);
            preparedStatement.setInt(9, user_id);

            // Execute the insert operation
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

}
