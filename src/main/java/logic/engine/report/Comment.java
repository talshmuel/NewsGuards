package logic.engine.report;

import data.transfer.object.report.CommentDTO;
import logic.engine.user.User;
import newsGuardServer.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Comment {
    private static int IDGenerator = 0;
    private int ID;
    private int reportID;
    private String text;
    private int writerID;
    boolean isAGuardComment;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;

    public Comment(int reportID, String text, int writerID, boolean isAGuardComment) {
        this.reportID = reportID;
        this.text = text;
        this.writerID = writerID;
        this.isAGuardComment = isAGuardComment;
        ID = ++IDGenerator;
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
        return new CommentDTO(reportID, text, writerID, isAGuardComment);
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
}
