package logic.engine.report;

import data.transfer.object.report.NewReportDTO;
import data.transfer.object.report.ReportDTO;
import logic.engine.user.User;
import logic.engine.user.UsersManager;
import newsGuardServer.DatabaseConfig;

import java.awt.geom.Point2D;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class ReportsManager {
    private Map<Integer, Report> reports = new HashMap<>();;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;


    public List<ReportDTO> getLastTwentyReportsToHomePage()
    {
        int lastReportID = 0;
        List<ReportDTO> reportDTOs = new ArrayList<>();
        String sql = "SELECT id FROM last_report_id";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery(sql)) {

                // Ensure there's data in the result set
                if (resultSet.next()) {
                    // Retrieve the value of the 'id' column
                    lastReportID =  resultSet.getInt("id");
                } else {
                    throw new SQLException("No data found in last_report_id table.");
                }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }

        for(int i = lastReportID; i >= lastReportID - 20; i--)
        {
            Report report = findAndRestoreReportFromDB(i);
            ReportDTO reportDTO = report.getReportDTO();
            reportDTOs.add(reportDTO);
        }
            return reportDTOs;
    }
    public Report addNewReport(NewReportDTO newReportDTO, User reporter){
        Report newReport = new Report(newReportDTO.getText(), newReportDTO.getImageURL(),
                reporter, newReportDTO.isAnonymousReport()
                , new Point2D.Double(newReportDTO.getLatitude(),
                newReportDTO.getLongitude()), newReportDTO.getDateTime(),0,false); //לבדוק את ציון הריפורט!!!!
        reports.put(newReport.getID(), newReport);
        newReport.pushReportToDB((reporter.getID()));
        return newReport;
    }

    public void addOrRemoveLike(int reportID, int userID){
        Report report = reports.get(reportID);
        if(report == null) {
            if (findAndRestoreReportFromDB(reportID) == null)
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
        String query = "SELECT  text, user_id, report_rate, imageurl, is_anonymous_report, time_reported, location_x, location_y " +
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
                    int reportRate = rs.getInt("report_rate");
                    String imageURL = rs.getString("imageurl");
                    boolean isAnonymousReport = rs.getBoolean("is_anonymous_report");
                    Date timeReported = (java.util.Date)(rs.getDate("time_reported"));
                    double locationX = rs.getDouble("location_x");
                    double locationY = rs.getDouble("location_y");

                    // Create NewUserDTO and User objects

                    User reporter = UsersManager.findAndRestoreUserByIDFromDB(reporterID);
                    Point2D.Double location = new Point2D.Double(locationX, locationY);
                    Report report = new Report(text, imageURL, reporter,isAnonymousReport,location,timeReported, reportRate, true);
                    report.restoreComments();
                    report.restoreLikes();
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

}
