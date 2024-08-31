package logic.engine.report;

import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.ReportDTO;
import logic.engine.location.history.management.OpenCageGeocodingService;
import logic.engine.reliability.management.Verification;
import logic.engine.user.User;
import newsGuardServer.DatabaseConfig;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.awt.geom.Point2D;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
//import java.util.Date;

public class Report {
    private int ID;
    private String text;
    private String imageURL;
    private final ArrayList<Integer> usersWhoLiked;
    private  int countUsersWhoLiked;
    private final ArrayList<Comment> comments;
    private Map<Integer, Verification> guardsVerifications; //  todo Nitzan change in DB
    private float reliabilityRate;
    private User reporter;
    private boolean isAnonymousReport;
    private Point2D.Double location;
    //private Date timeReported;
    private LocalDateTime timeReported; // Ensure this is LocalDateTime
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;

    public Report(String text, String imageURL, User reporter, boolean isAnonymousReport, Point2D.Double location, LocalDateTime timeReported, float reliabilityRate, boolean isReportRestoration, int likesNumber) {
        if(!isReportRestoration) {
            createNewID();
        }
        this.text = text;
        this.imageURL = imageURL;
        this.isAnonymousReport = isAnonymousReport;
        this.location = location;
        this.timeReported = timeReported;
        this.reliabilityRate = reliabilityRate;
        this.reporter = reporter;
        this.usersWhoLiked = new ArrayList<>();
        this.countUsersWhoLiked = likesNumber;
        this.comments = new ArrayList<>();
        this.guardsVerifications = new HashMap<>();
    }
    public int getID() {
        return ID;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReliabilityRate(float reliabilityRate) {
        this.reliabilityRate = reliabilityRate;
    }
    public void setReliabilityRateInDB(float reliabilityRate) {
        String sql = "UPDATE reports SET report_rate = ? WHERE report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setFloat(1, reliabilityRate);
            preparedStatement.setInt(2, ID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

    public void restoreReportID(int report_id){ ID = report_id;}

//    public void setReporter(User reporter){
//        this.reporter = reporter;
//    }
    public ArrayList<Comment> getComments()
    {
        return comments;
    }
    public int getCountUsersWhoLiked() {return countUsersWhoLiked;}
    public void addOrRemoveLike(int userID){
        if(usersWhoLiked.contains(userID)){
            usersWhoLiked.remove(userID);
            countUsersWhoLiked--;
            removeLikeFromDatabase(userID);
        }
        else if(likeExistsInDB(userID))
        {
            removeLikeFromDatabase(userID);
        }
        else {
            usersWhoLiked.add(userID);
            countUsersWhoLiked = usersWhoLiked.size();
            addLikeToDatabase(userID);
        }
    }


    public void addNewComment(Comment comment){
        comments.add(comment);
        comment.addCommentToDatabase();
    }

    public void setGuards(List<Integer> guards) {
        for(Integer guardID : guards){
            this.guardsVerifications.put(guardID, Verification.Pending);
        }
    }

    public String getCountry() throws Exception {
        //GeocodingService geocodingService = new GeocodingService();
        //String country = geocodingService.getCountry(location);

        //String country = NominatimExample.getCountry(location);
        OpenCageGeocodingService openCageGeocodingService = new OpenCageGeocodingService();
        String country = openCageGeocodingService.getCountry(location);
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
        String dateTimeISO = timeReported.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return new ReportDTO(ID ,text, imageURL, usersWhoLiked,countUsersWhoLiked, commentsDTO,
                reliabilityRate, reporter.getID(), reporter.createFullName(), isAnonymousReport,
                location, dateTimeISO);
    }
    public void pushReportToDB(int reporter_id)
    {
//        java.sql.Date sqlDate = new java.sql.Date(timeReported.getDate());
//        java.sql.Date sqlTime = new java.sql.Date(timeReported.getTime());
        Timestamp timestamp = Timestamp.valueOf(timeReported);

        String sql = "INSERT INTO reports (report_id, text, user_id, report_rate, imageurl, is_anonymous_report, time_reported, location_x, location_y, likes_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Establish a database connection
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the parameters
            preparedStatement.setInt(1,ID);
            preparedStatement.setString(2, text);
            preparedStatement.setInt(3, reporter_id);
            preparedStatement.setFloat(4, reliabilityRate);
            preparedStatement.setString(5, imageURL);
            preparedStatement.setBoolean(6, isAnonymousReport);
            preparedStatement.setTimestamp(7, timestamp); // Use Timestamp for time_reported
            preparedStatement.setDouble(8, location.x);
            preparedStatement.setDouble(9, location.y);
            preparedStatement.setInt(10, countUsersWhoLiked);


            // Execute the insert operation
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    } //צריך לאתחל את report_rate

    public void removeLikeFromDatabase(int userID) {
        int likesNumber = 0;
        String sql = "DELETE FROM likes WHERE user_id = ? AND report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, ID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }

        String sqlSelect = "SELECT likes_number FROM reports WHERE report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatementSelect = connection.prepareStatement(sqlSelect)) {

            // Set the parameter for the SELECT query
            preparedStatementSelect.setInt(1, ID);

            // Execute the SELECT query
            try (ResultSet resultSet = preparedStatementSelect.executeQuery()) {
                // Ensure there's data in the result set
                if (resultSet.next()) {
                    // Retrieve the value of the 'likes_number' column
                    likesNumber = resultSet.getInt("likes_number");
                    likesNumber--; // Decrement the value
                } else {
                    System.out.println("No record found for report ID: " + ID);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }

        sql = "UPDATE reports SET likes_number = ? WHERE report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, likesNumber);
            preparedStatement.setInt(2, ID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

    public void addLikeToDatabase(int userID) {
        int likesNumber = 0;
        String sql = "INSERT INTO likes (report_id, user_id) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, ID);
            preparedStatement.setInt(2, userID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }

        String sqlSelect = "SELECT likes_number FROM reports WHERE report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatementSelect = connection.prepareStatement(sqlSelect)) {

            // Set the parameter for the SELECT query
            preparedStatementSelect.setInt(1, ID);

            // Execute the SELECT query
            try (ResultSet resultSet = preparedStatementSelect.executeQuery()) {
                // Ensure there's data in the result set
                if (resultSet.next()) {
                    // Retrieve the value of the 'likes_number' column
                    likesNumber = resultSet.getInt("likes_number");
                    likesNumber++; // increment the value
                } else {
                    System.out.println("No record found for report ID: " + ID);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }

        sql = "UPDATE reports SET likes_number = ? WHERE report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, likesNumber);
            preparedStatement.setInt(2, ID);
            preparedStatement.executeUpdate();

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
    private void createNewID()
    {
        String selectQuery = "SELECT id FROM last_report_id LIMIT 1";
        String updateQuery = "UPDATE last_report_id SET id = ?";
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet rs = preparedStatement.executeQuery()) {

            if (rs.next()) {
                int lastreportId = rs.getInt("id");
                ID = ++lastreportId;
                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, ID);
                    int rowsUpdated = updateStmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Report ID updated successfully to: " + ID);
                    } else {
                        System.out.println("Failed to update Report ID.");
                    }
                }
            } else {
                System.out.println("No Report ID found.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

    public void restoreComments()
    {
        String query = "SELECT user_id, user_name, comment_id, text, isaguardcomment FROM comments WHERE report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    int commentId = rs.getInt("comment_id");
                    String text = rs.getString("text");
                    String userName = rs.getString("user_name");
                    boolean isGuardComment = rs.getBoolean("isaguardcomment");

                    // Create a Comment object
                    Comment comment = new Comment(ID, text, userId, isGuardComment, true, commentId, userName);
                    comments.add(comment);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions (e.g., log errors)
        }
    }
    public void restoreLikes()
    {
        String query = "SELECT user_id FROM likes WHERE report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    usersWhoLiked.add(userId);
                }
                countUsersWhoLiked = usersWhoLiked.size();
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions (e.g., log errors)
        }
    }
//    public void restoreGuardsVerifications()
//    {
//        String query = "SELECT (user_id, user_response) FROM guards_verification WHERE report_id = ?";
//
//        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
//             PreparedStatement stmt = connection.prepareStatement(query)) {
//
//            stmt.setInt(1, ID);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                while (rs.next()) {
//                    int userId = rs.getInt("user_id");
//                    int userResponseInteger = rs.getInt("user_response");
//                    Verification userResponse = Verification.fromInt(userResponseInteger);
//                   guardsVerifications.put(userId, userResponse);
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle exceptions (e.g., log errors)
//        }
//    }

    public void restoreGuardsVerifications() {
        String query = "SELECT user_id, user_response FROM guards_verification WHERE report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, ID);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    int userResponseInteger = rs.getInt("user_response");
                    Verification userResponse = Verification.fromInt(userResponseInteger);
                    guardsVerifications.put(userId, userResponse);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions (e.g., log errors)
        }
    }

    public void storeGuardsInDB(ArrayList<Integer> guardsID)
    {
        String sql = "INSERT INTO guards_verification (report_id, user_id, user_response) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (Integer integer : guardsID) {
                preparedStatement.setInt(1, ID);
                preparedStatement.setInt(2, integer);
                preparedStatement.setInt(3, 3);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

    public void updateGuardVerification(int guardID, Verification verification){
        guardsVerifications.put(guardID, verification);
        updateGuardVerificationInDB(guardID, verification);
    }

    public void updateGuardVerificationInDB(int guardID, Verification verification) {
        int verificationInt = verification.toInt();
        String sql = "UPDATE guards_verification SET user_response = ?  WHERE report_id = ? AND user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, verificationInt);
            preparedStatement.setInt(2, ID);
            preparedStatement.setInt(3, guardID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

}
