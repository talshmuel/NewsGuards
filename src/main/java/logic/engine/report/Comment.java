package logic.engine.report;

import data.transfer.object.report.CommentDTO;
import logic.engine.user.User;
import newsGuardServer.DatabaseConfig;

import java.sql.*;

public class Comment {
    private int ID;
    private int reportID;
    private String text;
    private String commenterFullName;
    private int writerID;
    boolean isAGuardComment;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;

    public Comment(int reportID, String text, int writerID, boolean isAGuardComment,boolean isRestoringComment, int commentId, String commenterFullName) {
        if(!isRestoringComment){
            createNewID();
        }
        else {
            ID = commentId;
        }
        this.reportID = reportID;
        this.text = text;
        this.writerID = writerID;
        this.isAGuardComment = isAGuardComment;
        this.commenterFullName = commenterFullName;
    }

    public int getReportID() {
        return reportID;
    }

    public String getText() {
        return text;
    }

    public int getWriterID() {
        return writerID;
    }

    public boolean isAGuardComment() {
        return isAGuardComment;
    }
    public CommentDTO getCommentDTO(){
        return new CommentDTO(reportID, text, writerID, isAGuardComment, commenterFullName);
    }

    public void addCommentToDatabase() {
        String sql = "INSERT INTO comments (user_id, comment_id, report_id, text, isaguardcomment) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, writerID);
            preparedStatement.setInt(2, ID);
            preparedStatement.setInt(3, reportID);
            preparedStatement.setString(4, text);
            preparedStatement.setBoolean(5, isAGuardComment);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }

    private void createNewID()
    {
        String selectQuery = "SELECT id FROM last_comment_id LIMIT 1";
        String updateQuery = "UPDATE last_comment_id SET id = ?";
        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
             ResultSet rs = preparedStatement.executeQuery()) {

            if (rs.next()) {
                int lastCommentId = rs.getInt("id");
                ID = ++lastCommentId;
                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, ID);
                    int rowsUpdated = updateStmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Comment ID updated successfully to: " + ID);
                    } else {
                        System.out.println("Failed to update Comment ID.");
                    }
                }
            } else {
                System.out.println("No Comment ID found.");
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }
}
