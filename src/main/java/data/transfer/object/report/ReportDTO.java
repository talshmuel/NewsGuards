package data.transfer.object.report;

import logic.engine.reliability.management.GuardVerification;

import java.awt.geom.Point2D;
import java.util.*;

public final class ReportDTO {
    private int reportID;
    private String text;
    private String imageURL;
    private Set<Integer> IDOfUsersWhoLiked;
    private ArrayList<CommentDTO> comments;
    //Map<Integer, GuardVerification> guardsResponses;
    private float reliabilityRate;
    private int reporterID;
    private String reporterFullName;
    private boolean isAnonymousReport;
    private Point2D.Double location;
    private Date timeReported;


    public ReportDTO(int reportID, String text, String imageURL, Set<Integer> IDOfUsersWhoLiked, ArrayList<CommentDTO> comments, float reliabilityRate, int reporterID, String reporterFullName, boolean isAnonymousReport, Point2D.Double location, Date timeReported) {
        this.reportID = reportID;
        this.text = text;
        this.imageURL = imageURL;
        this.IDOfUsersWhoLiked = IDOfUsersWhoLiked;
        this.comments = comments;
        //this.guardsResponses = guardsResponses;
        this.reliabilityRate = reliabilityRate;
        this.reporterID = reporterID;
        this.reporterFullName = reporterFullName;
        this.isAnonymousReport = isAnonymousReport;
        this.location = location;
        this.timeReported = timeReported;
    }

    public String getText() {
        return text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Set<Integer> getIDOfUsersWhoLiked() {
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
        return isAnonymousReport;
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
}
