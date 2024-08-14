package logic.engine.user;

import data.transfer.object.user.NewUserDTO;
import logic.engine.notification.Notification;
import logic.engine.reliability.management.Rate;
import logic.engine.report.Comment;
import logic.engine.report.Report;
import logic.engine.user.registration.UserRegistrationDetails;
import newsGuardServer.DatabaseConfig;

import java.sql.*;
import java.util.*;

public class User {
    private int ID;
    private UserRegistrationDetails registrationDetails;
    private ArrayList<Report> reports;
    private ArrayList<Notification> notifications;
    private ArrayList<Report> reportsThatTheUserIsAGuardOf;
    private Rate reliabilityRate;
    private ArrayList<Integer> taggedReports;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;


    public User(NewUserDTO newUserData, Boolean isUserRestoration)
    {
        if(isUserRestoration) {
            restoreUser(newUserData);
        }
        else{
            createNewID();
            reliabilityRate = Rate.THREE;
        }
        this.registrationDetails = getRegistrationDetails(newUserData);
        reports = new ArrayList<>();
        notifications = new ArrayList<>();
        reportsThatTheUserIsAGuardOf = new ArrayList<>();
        taggedReports = new ArrayList<>();
    }
    public String getEmail(){
        return registrationDetails.getEmail();
    }

    public int getID() {
        return ID;
    }

    public UserRegistrationDetails getRegistrationDetails() {
        return registrationDetails;
    }

    private UserRegistrationDetails getRegistrationDetails(NewUserDTO newUserData){

        return new UserRegistrationDetails(newUserData.getFirstName(), newUserData.getLastName(),
                newUserData.getCountry(), newUserData.getEmail(), newUserData.getPassword(),
                newUserData.getImageURL(), newUserData.getPhoneNumber(),
                newUserData.isLocationAccessPermission());

    }
    public boolean checkUserPassword(String passwordToCheck){
        return registrationDetails.checkUserPassword(passwordToCheck);
    }
//    public void addNewReport(Report newReport){
//        reports.add(newReport);
//        newReport.setReporter(this);
//    }
    private void createNewID()
    {
        String selectQuery = "SELECT id FROM last_user_id LIMIT 1";
        String updateQuery = "UPDATE last_user_id SET id = ?";
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet rs = preparedStatement.executeQuery()) {

                if (rs.next()) {
                    int lastUserId = rs.getInt("id");
                    ID = ++lastUserId;
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                        updateStmt.setInt(1, ID);
                        int rowsUpdated = updateStmt.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("user ID updated successfully to: " + ID);
                        } else {
                            System.out.println("Failed to update user ID.");
                        }
                    }
                } else {
                    System.out.println("No user ID found.");
                }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

    private void restoreUser(NewUserDTO newUserData)
    {
        String sql = "SELECT user_id, reliability_scale FROM users WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, newUserData.getEmail());
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    ID = rs.getInt("user_id");
                    int reliabilityRateInt = rs.getInt("reliability_scale");
                    reliabilityRate = Rate.convertIntToRate(reliabilityRateInt);
                    System.out.println("User ID: " + ID);
                } else {
                    System.out.println("No user found with the given email.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

    public void pushUserToDB()
    {
        String sql = "INSERT INTO users (user_id, reliabillity_scale) " +
                "VALUES (?, ?)";

        // Establish a database connection
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(),DB_CONFIG.getUsername(),DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the parameters
            preparedStatement.setInt(1, ID);
            preparedStatement.setInt(2, reliabilityRate.getValue());

            // Execute the insert operation
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }

        registrationDetails.pushRegistrationDetailsToDB(ID);
    }

}
