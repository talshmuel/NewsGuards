package data.transfer.object.report;

public final class CommentDTO {
    private final int reportID;
    private final String text;
    private final String commenterFullName;
    private final int commenterUserID;
    private final boolean isAGuardComment;

    public CommentDTO(int reportID, String text, int commenterUserID, boolean isAGuardComment, String commenterFullName) {
        this.reportID = reportID;
        this.text = text;
        this.commenterUserID = commenterUserID;
        this.isAGuardComment = isAGuardComment;
        this.commenterFullName = commenterFullName;
    }

    public String getText() {
        return text;
    }

    public int getCommenterUserID() {
        return commenterUserID;
    }

    public boolean isAGuardComment() {
        return isAGuardComment;
    }

    public int getReportID() {
        return reportID;
    }

    public String getCommenterFullName() {
        return commenterFullName;
    }
}
