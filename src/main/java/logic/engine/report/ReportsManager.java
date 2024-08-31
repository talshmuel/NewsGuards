package logic.engine.report;

import data.transfer.object.report.NewReportDTO;
import data.transfer.object.report.ReportDTO;
import logic.engine.reliability.management.Verification;
import logic.engine.user.User;
import logic.engine.user.UsersManager;
import newsGuardServer.DatabaseConfig;

import java.awt.geom.Point2D;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class ReportsManager {
    private Map<Integer, Report> reports = new HashMap<>();;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;


    public ArrayList<ReportDTO> getLastTwentyReportsToHomePage()
    {
        int lastReportID = 0;
        ArrayList<ReportDTO> reportDTOs = new ArrayList<>();
        String sql = "SELECT id FROM last_report_id";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) { // Execute the query without passing the SQL string
            // Ensure there's data in the result set
            if (resultSet.next()) {
                // Retrieve the value of the 'id' column
                lastReportID = resultSet.getInt("id");
            } else {
                throw new SQLException("No data found in last_report_id table.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
        for(int i = lastReportID; i >= lastReportID - 5; i--)
        {
            Report report;
            if(reports.containsKey(i))
                report = reports.get(i);
            else
                 report = findAndRestoreReportFromDB(i);
            if(report != null)
            {
                ReportDTO reportDTO = report.getReportDTO();
                reportDTOs.add(reportDTO);
            }
        }
            return reportDTOs;
    }
    public Report addNewReport(NewReportDTO newReportDTO, User reporter) {
        LocalDateTime timeReported = LocalDateTime.parse(newReportDTO.getTimeReported(), DateTimeFormatter.ISO_DATE_TIME);
        Report newReport = new Report(newReportDTO.getText(), newReportDTO.getImageURL(),
                reporter, newReportDTO.isAnonymousReport()
                , new Point2D.Double(newReportDTO.getLatitude(),
                newReportDTO.getLongitude()), timeReported, 0, false, 0); //לבדוק את ציון הריפורט!!!!
        reports.put(newReport.getID(), newReport);
        newReport.pushReportToDB((reporter.getID()));
        return newReport;
    }

    public void addOrRemoveLike(int reportID, int userID){
        Report report = reports.get(reportID);
        if(report == null) {
            report = findAndRestoreReportFromDB(reportID);
            if (report == null)
                throw new NoSuchElementException("Error - there is no report in the system whose ID number is: " + reportID);
        }
        report.addOrRemoveLike(userID);
    }
    public void addNewComment(Comment comment){
        Report report = reports.get(comment.getReportID());
        if(report == null){
            report = findAndRestoreReportFromDB(comment.getReportID());
            if (report == null)
                throw new NoSuchElementException("Error - there is no report in the system whose ID number is:" + comment.getReportID());
        }
        reports.put(report.getID(), report);
        report.addNewComment(comment);
    }
    public Report findAndRestoreReportFromDB(int reportID){
        String query = "SELECT  text, user_id, report_rate, imageurl, is_anonymous_report, time_reported, location_x, location_y, likes_number " +
                "FROM reports WHERE report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, reportID);

            try (ResultSet rs = stmt.executeQuery()) {
                // Check if result set contains a row
                if (rs.next()) {
                    // Extract user details
                    String text = rs.getString("text");
                    int reporterID = rs.getInt("user_id");
                    float reportRate = rs.getFloat("report_rate");
                    String imageURL = rs.getString("imageurl");
                    boolean isAnonymousReport = rs.getBoolean("is_anonymous_report");
                    Timestamp timeReportedTimestamp = rs.getTimestamp("time_reported");

                    // Convert SQL Date and Timestamp to LocalDate and LocalDateTime
                    LocalDateTime timeReported = timeReportedTimestamp.toLocalDateTime(); // Convert Timestamp to LocalDateTime
                    double locationX = rs.getDouble("location_x");
                    double locationY = rs.getDouble("location_y");
                    int likesNumber = rs.getInt("likes_number");

                    // Create NewUserDTO and User objects

                    User reporter = UsersManager.findAndRestoreUserByIDFromDB(reporterID);
                    Point2D.Double location = new Point2D.Double(locationX, locationY);
                    Report report = new Report(text, imageURL, reporter,isAnonymousReport,location,timeReported, reportRate, true,likesNumber);
                    report.restoreReportID(reportID);
                    report.restoreComments();
                    report.restoreLikes();
                    report.restoreGuardsVerifications();
                    reports.put(reportID,report);
                    return report;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateGuardVerification(int reportID, int guardID, Verification verification){
        reports.get(reportID).updateGuardVerification(guardID, verification);
    }

}
