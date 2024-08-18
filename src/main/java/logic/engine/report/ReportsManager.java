package logic.engine.report;

import data.transfer.object.report.NewReportDTO;
import data.transfer.object.report.ReportDTO;
import data.transfer.object.user.NewUserDTO;
import logic.engine.user.User;
import logic.engine.user.UsersManager;
import newsGuardServer.DatabaseConfig;

import java.awt.geom.Point2D;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class ReportsManager {
    private Map<Integer, Report> reports;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;

    public ReportsManager() {
        this.reports = new HashMap<>();
    }

//    public List<ReportDTO> getLastTwentyReportsToHomePage()
//    {
//
//    }
    public Report addNewReport(NewReportDTO newReportDTO, User reporter){
        Report newReport = new Report(newReportDTO.getText(), newReportDTO.getImageURL(),
                reporter, newReportDTO.isAnonymousReport()
                , new Point2D.Double(newReportDTO.getLatitude(),
                newReportDTO.getLongitude()), newReportDTO.getDateTime(),0); //לבדוק את ציון הריפורט!!!!
        reports.put(newReport.getID(), newReport);
        newReport.pushReportToDB((reporter.getID()));
        return newReport;
    }

    public void addOrRemoveLike(int reportID, int userID){
        Report report = reports.get(reportID);
        if(report == null) {
            if (findAndCreateReportInDB(reportID) == null)
                throw new NoSuchElementException("Error - there is no report in the system whose ID number is: " + reportID);
        }
        report.addOrRemoveLike(userID);
    }
    public void addNewComment(Comment comment){
        Report report = reports.get(comment.getReportID());
        if(report == null){
            report = findAndCreateReportInDB(comment.getReportID());
            if (report == null)
                throw new NoSuchElementException("Error - there is no report in the system whose ID number is:" + comment.getReportID());
        }
        reports.put(report.getID(), report);
        report.addNewComment(comment);
    }
    public Report findAndCreateReportInDB(int reportID){
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
                    User reporter = UsersManager.findAndCreateUserByIDInDB(reporterID);
                    Point2D.Double location = new Point2D.Double(locationX, locationY);

                    return new Report(text, imageURL, reporter,isAnonymousReport,location,timeReported, reportRate);
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
