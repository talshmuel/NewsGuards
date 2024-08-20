package data.transfer.object.report;

import javax.xml.stream.Location;
import java.awt.geom.Point2D;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public final class ReportDTO {
    private String text;
    private String imageURL;
    private ArrayList<Integer> IDOfUsersWhoLiked;
    private ArrayList<CommentDTO> comments;
    private ArrayList<Integer> guardsID;
    private float reliabilityRate;
    private int reporterID;
    private boolean isAnonymousReport;
    private Point2D.Double location;
    private Date timeReported;

    public ReportDTO(String text, String imageURL, ArrayList<Integer> IDOfUsersWhoLiked, ArrayList<CommentDTO> comments, ArrayList<Integer> guardsID, float reliabilityRate, int reporterID, boolean isAnonymousReport, Point2D.Double location, Date timeReported) {
        this.text = text;
        this.imageURL = imageURL;
        this.IDOfUsersWhoLiked = IDOfUsersWhoLiked;
        this.comments = comments;
        this.guardsID = guardsID;
        this.reliabilityRate = reliabilityRate;
        this.reporterID = reporterID;
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

    public ArrayList<Integer> getIDOfUsersWhoLiked() {
        return IDOfUsersWhoLiked;
    }

    public ArrayList<CommentDTO> getComments() {
        return comments;
    }

    public ArrayList<Integer> getGuardsID() {
        return guardsID;
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
}
