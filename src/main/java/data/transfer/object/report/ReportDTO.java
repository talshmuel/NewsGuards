package data.transfer.object.report;

import java.awt.geom.Point2D;
import java.util.*;

public final class ReportDTO {
    private int reportID;
    private String text;
    private String imageURL;
    private ArrayList<Integer> IDOfUsersWhoLiked;
    private ArrayList<CommentDTO> comments;
    private float reliabilityRate;
    private int reporterID;
    private String reporterFullName;
    private boolean anonymousReport;
    private Point2D.Double location;
    private Date timeReported; // Ensure this is LocalDateTime
    private int countUsersWhoLiked;

    private float reporterReliabilityRate;

    public ReportDTO(int reportID, String text, String imageURL, ArrayList<Integer> IDOfUsersWhoLiked,int likesNumber, ArrayList<CommentDTO> comments, float reliabilityRate, int reporterID, String reporterFullName, boolean anonymousReport, Point2D.Double location, Date timeReported, float reporterReliabilityRate) {
        this.reportID = reportID;
        this.text = text;
        this.imageURL = imageURL;
        this.IDOfUsersWhoLiked = IDOfUsersWhoLiked;
        this.countUsersWhoLiked = likesNumber;
        this.comments = comments;
        this.reliabilityRate = reliabilityRate;
        this.reporterID = reporterID;
        this.reporterFullName = reporterFullName;
        this.anonymousReport = anonymousReport;
        this.location = location;
        this.timeReported = timeReported;
        this.reporterReliabilityRate = reporterReliabilityRate;
    }

    public String getText() {
        return text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public ArrayList<Integer> getIDOfUsersWhoLiked() {
        return IDOfUsersWhoLiked;
    }

    public ArrayList<CommentDTO> getComments() {
        return comments;
    }



    public float getReliabilityRate() {
        return reliabilityRate;
    }

    public int getReporterID() {
        return reporterID;
    }

    public boolean isAnonymousReport() {
        return anonymousReport;
    }

    public Point2D.Double getLocation() {
        return location;
    }

    public Date getTimeReported() {
        return timeReported;
    }

    public String getReporterFullName() {
        return reporterFullName;
    }

    public int getReportID() {
        return reportID;
    }

    public int getCountUsersWhoLiked() {
        return countUsersWhoLiked;
    }

    public float getReporterReliabilityRate(){return reporterReliabilityRate;}

}
