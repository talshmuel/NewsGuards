package logic.engine.user;

import data.transfer.object.report.ReportDTO;
import data.transfer.object.user.NewUserDTO;
import data.transfer.object.user.UserDTO;
import logic.engine.notification.Notification;
import logic.engine.reliability.management.Rate;
import logic.engine.report.Comment;
import logic.engine.report.Report;
import logic.engine.user.registration.UserRegistrationDetails;
import newsGuardServer.DatabaseConfig;

import java.awt.geom.Point2D;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class User {
    private int ID;
    private UserRegistrationDetails registrationDetails;
    private ArrayList<Report> reports = new ArrayList<>();
    private ArrayList<Notification> notifications;
    private ArrayList<Report> reportsThatTheUserIsAGuardOf;
    private float reliabilityRate;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;


    public User(NewUserDTO newUserData, float reliability_Rate, Boolean isUserRestoration)
    {
        if(isUserRestoration) {
            restoreUser(newUserData);
            restoreUserReports();
            //restoreUserGuardReports();
        }
        else{
            createNewID();
            reportsThatTheUserIsAGuardOf = new ArrayList<>();
        }
        reliabilityRate = reliability_Rate;
        this.registrationDetails = getRegistrationDetails(newUserData);
        //notifications = new ArrayList<>();
    }
    public void restoreUserID(int user_id){ ID = user_id;}

    public String createFullName()
    {
        return registrationDetails.getFirstName() + " " + registrationDetails.getLastName();
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
                    reliabilityRate = rs.getFloat("reliability_scale");
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
        String sql = "INSERT INTO users (user_id, reliability_scale) " +
                "VALUES (?, ?)";

        // Establish a database connection
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(),DB_CONFIG.getUsername(),DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the parameters
            preparedStatement.setInt(1, ID);
            preparedStatement.setFloat(2, reliabilityRate);

            // Execute the insert operation
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }

        registrationDetails.pushRegistrationDetailsToDB(ID);
    }

    private void restoreUserReports() {
        String query = "SELECT report_id, text, user_id, report_rate, imageurl, is_anonymous_report, time_reported, location_x, location_y " +
                "FROM reports WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set the reporterID parameter
            stmt.setInt(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {
                // Loop through each row in the result set
                while (rs.next()) {
                    int reportID = rs.getInt("report_id");
                    String text = rs.getString("text");
                    int user_id = rs.getInt("user_id");
                    int reportRate = rs.getInt("report_rate");
                    String imageURL = rs.getString("imageurl");
                    boolean isAnonymousReport = rs.getBoolean("is_anonymous_report");
                    Date timeReported = rs.getDate("time_reported");
                    double locationX = rs.getDouble("location_x");
                    double locationY = rs.getDouble("location_y");

                    Point2D.Double location = new Point2D.Double(locationX, locationY);
                    Report report = new Report(text, imageURL, this, isAnonymousReport, location, timeReported, reportRate,true);

                    reports.add(report);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle exceptions as needed
        }
    }


//    private void restoreUserGuardReports()
//    {
//        String query = "SELECT report_id, text, user_id, report_rate, imageurl, is_anonymous_report, time_reported, location_x, location_y " +
//                "FROM reports WHERE user_id = ? and ";
//
//        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//
//            // Set the reporterID parameter
//            stmt.setInt(1, ID);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                // Loop through each row in the result set
//                while (rs.next()) {
//                    int reportID = rs.getInt("report_id");
//                    String text = rs.getString("text");
//                    int user_id = rs.getInt("user_id");
//                    int reportRate = rs.getInt("report_rate");
//                    String imageURL = rs.getString("imageurl");
//                    boolean isAnonymousReport = rs.getBoolean("is_anonymous_report");
//                    Date timeReported = rs.getDate("time_reported");
//                    double locationX = rs.getDouble("location_x");
//                    double locationY = rs.getDouble("location_y");
//
//                    Point2D.Double location = new Point2D.Double(locationX, locationY);
//                    Report report = new Report(text, imageURL, this, isAnonymousReport, location, timeReported, reportRate,true);
//
//                    reports.add(report);
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace(); // Log or handle exceptions as needed
//        }
//    }

    public UserDTO gerUserDTO(){
        ArrayList<ReportDTO> reportDTOS = new ArrayList<>();
        ArrayList<ReportDTO> reportThatTheUserIsGuardOfDTOS = new ArrayList<>();

        for(Report report : reports){
            reportDTOS.add(report.getReportDTO());
        }
        for(Report report : reportsThatTheUserIsAGuardOf){
            reportThatTheUserIsGuardOfDTOS.add(report.getReportDTO());
        }


        return new UserDTO(ID, registrationDetails.getFirstName(), registrationDetails.getLastName(), registrationDetails.getCountry(),
                registrationDetails.getEmail(), registrationDetails.getImageURL(), registrationDetails.getPhoneNumber(), registrationDetails.getLocationAccessPermission(),
                reportDTOS, reportThatTheUserIsGuardOfDTOS, reliabilityRate);


    }

}
