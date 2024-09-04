package logic.engine.location.history.management;

import newsGuardServer.DatabaseConfig;

import java.awt.geom.Point2D;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationHistoryManager {
    double radiusMetersToLookingForUsers = 50;//todo:maybe change
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;

    public void saveUserLocation(int userID, double lat, double lon) {
        String sql = "UPDATE users_location SET latitude = ?, longitude = ? WHERE user_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, lat);
            preparedStatement.setDouble(2, lon);
            preparedStatement.setInt(3, userID);

            // Execute the insert operation
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

    public ArrayList<Integer> findUsersInRadius(int reporterID, double lat, double lon){
        ArrayList<Integer> userIds = new ArrayList<>();
        String sql = "SELECT user_id FROM users_location " +
                "WHERE ( " +
                "    6371000 * ACOS( " +
                "        COS(RADIANS(?)) * COS(RADIANS(latitude)) * " +
                "        COS(RADIANS(longitude) - RADIANS(?)) + " +
                "        SIN(RADIANS(?)) * SIN(RADIANS(latitude)) " +
                "    ) " +
                ") <= ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(),DB_CONFIG.getUsername(),DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, lat);  // Latitude parameter
            preparedStatement.setDouble(2, lon);  // Longitude parameter
            preparedStatement.setDouble(3, lat);  // Latitude parameter (again for sine calculation)
            preparedStatement.setDouble(4, radiusMetersToLookingForUsers);  // Latitude parameter (again for sine calculation)

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    userIds.add(rs.getInt("user_id"));
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }

        userIds.remove(Integer.valueOf(reporterID));
        return userIds;
    }
}
//
//        //todo delete this (it is just a try)
//        ArrayList<Integer> res = new ArrayList<>();
//        res.add(1);
//        return res;