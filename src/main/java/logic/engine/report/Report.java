package logic.engine.report;

import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.ReportDTO;
import logic.engine.location.history.management.GeocodingService;
import logic.engine.location.history.management.NominatimExample;
import logic.engine.reliability.management.Rate;
import logic.engine.user.User;
import newsGuardServer.DatabaseConfig;
import org.springframework.jdbc.core.JdbcTemplate;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class Report {
    private final int ID;
    private static int IDGenerator = 0;
    private String text;
    private String imageURL;
    private final Set<Integer> usersWhoLiked;
    private final ArrayList<Comment> comments;
    private List<Integer> guards;
    private float reliabilityRate;
    private User reporter;
    private boolean isAnonymousReport;
    private Point2D.Double location;
    private Date timeReported;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;

    public Report(String text, String imageURL, User reporter, boolean isAnonymousReport, Point2D.Double location, Date timeReported) {
        this.text = text;
        this.imageURL = imageURL;
        this.usersWhoLiked = new HashSet<>();
        this.comments = new ArrayList<>();
        this.reporter = reporter;
        this.isAnonymousReport = isAnonymousReport;
        this.location = location;
        this.timeReported = timeReported;
        ID = ++IDGenerator;
        reporter.addNewReport(this);

    }
    public int getID() {
        return ID;
    }

    public void setReporter(User reporter){
        this.reporter = reporter;
    }
    public void addOrRemoveLike(int userID){
        if(usersWhoLiked.contains(userID)){
            usersWhoLiked.remove(userID);
            removeLikeFromDatabase(userID);
        }
        else if(likeExistsInDB(userID))
        {
            removeLikeFromDatabase(userID);
        }
        else {
            System.out.print("not contain");
            System.out.print(userID);

            usersWhoLiked.add(userID);
            addLikeToDatabase(userID);
        }
    }

    public void addNewComment(Comment comment){
        comments.add(comment);
    }

    public void setGuards(List<Integer> guards) {
        this.guards = guards;
    }

    public String getCountry() throws Exception {
        //GeocodingService geocodingService = new GeocodingService();
        //String country = geocodingService.getCountry(location);

        String country = NominatimExample.getCountry(location);

        if (country == null) {
            throw new NoSuchElementException("Country not found for the provided location");
        }
        return country;
    }

    public float getReliabilityRate() {
        return reliabilityRate;
    }
    public ReportDTO getReportDTO(){
        ArrayList<CommentDTO> commentsDTO = new ArrayList<>();

        for (Comment comment : comments){
            commentsDTO.add(comment.getCommentDTO());
        }

        return new ReportDTO(text, imageURL, new ArrayList<>(usersWhoLiked), commentsDTO, new ArrayList<>(guards),
                reliabilityRate, reporter.getID(), isAnonymousReport,
                location, timeReported);
    }
    public void pushReportToDB(int reporter_id)
    {
//        String sql = "INSERT INTO reports (report_id, text, user_id, likes_number, report_rate, image_url) VALUES (?, ?, ?, ?, ?, ?)";
//
//        jdbcTemplate.update(sql, ID, text, reporter_id, usersWhoLiked.size(), reliabilityRate, imageURL);

        String sql = "INSERT INTO reports (report_id, text, user_id, likes_number, report_rate, imageurl) VALUES (?, ?, ?, ?, ?, ?)";

        // Establish a database connection
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the parameters
            preparedStatement.setInt(1,ID);
            preparedStatement.setString(2, text);
            preparedStatement.setInt(3, reporter_id);
            preparedStatement.setInt(4, usersWhoLiked.size());
            preparedStatement.setFloat(5, reliabilityRate);
            preparedStatement.setString(6, imageURL);

            // Execute the insert operation
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    } //צריך לאתחל את report_rate

    public void removeLikeFromDatabase(int userID) {
        String sql = "DELETE FROM likes WHERE user_id = ? AND report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, ID);

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

    public void addLikeToDatabase(int userID) {
        String sql = "INSERT INTO likes (user_id, report_id) VALUES (?, ?)";
        System.out.print("the userid about to enter: " + userID);
        System.out.print("the reportid about to enter: " + ID);


        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, ID);
            System.out.print("HELLO");


        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

    public boolean likeExistsInDB(int userID) {
        String sql = "SELECT 1 FROM likes WHERE user_id = ? AND report_id = ?";
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, ID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // If a row is returned, the like exists
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
            return false; // Assuming the like does not exist in case of error
        }
    }
}
