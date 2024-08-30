package data.transfer.object.report;

public final class CommentDTO {
    private final int reportID;
    private final String text;
    private final String commenterFullName;
    private final int writerUserID;
    private final boolean isAGuardComment;

    public CommentDTO(int reportID, String text, int writerUserID, boolean isAGuardComment, String commenterFullName) {
        this.reportID = reportID;
        this.text = text;
        this.writerUserID = writerUserID;
        this.isAGuardComment = isAGuardComment;
        this.commenterFullName = commenterFullName;
    }

    public String getText() {
        return text;
    }

    public int getWriterUserID() {
        return writerUserID;
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
