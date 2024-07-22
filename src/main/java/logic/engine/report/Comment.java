package logic.engine.report;

import data.transfer.object.report.CommentDTO;
import logic.engine.user.User;

public class Comment {
    private static int IDGenerator = 0;
    private int ID;
    private int reportID;
    private String text;
    private int writerID;
    boolean isAGuardComment;

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
}
