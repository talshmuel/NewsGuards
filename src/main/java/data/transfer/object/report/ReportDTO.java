package data.transfer.object.report;

import javax.xml.stream.Location;
import java.awt.geom.Point2D;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public final class ReportDTO {
    private String text;
    private String imageURL;
    private Set<Integer> IDOfUsersWhoLiked;
    private ArrayList<CommentDTO> comments;
    private List<Integer> guardsID;
    private float reliabilityRate;
    private int reporterID;
    private boolean isAnonymousReport;
    private Point2D.Double location;
    private Date timeReported;

    public ReportDTO(String text, String imageURL, Set<Integer> IDOfUsersWhoLiked, ArrayList<CommentDTO> comments, List<Integer> guardsID, float reliabilityRate, int reporterID, boolean isAnonymousReport, Point2D.Double location, Date timeReported) {
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

    // Getter and setter for text
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    // Getter and setter for imageURL
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    // Getter and setter for IDOfUsersWhoLiked
    public Set<Integer> getIDOfUsersWhoLiked() {
        return IDOfUsersWhoLiked;
    }

    public void setIDOfUsersWhoLiked(Set<Integer> IDOfUsersWhoLiked) {
        this.IDOfUsersWhoLiked = IDOfUsersWhoLiked;
    }

    // Getter and setter for comments
    public ArrayList<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentDTO> comments) {
        this.comments = comments;
    }

    // Getter and setter for guardsID
    public List<Integer> getGuardsID() {
        return guardsID;
    }

    public void setGuardsID(List<Integer> guardsID) {
        this.guardsID = guardsID;
    }

    // Getter and setter for reliabilityRate
    public float getReliabilityRate() {
        return reliabilityRate;
    }

    public void setReliabilityRate(float reliabilityRate) {
        this.reliabilityRate = reliabilityRate;
    }

    // Getter and setter for reporterID
    public int getReporterID() {
        return reporterID;
    }

    public void setReporterID(int reporterID) {
        this.reporterID = reporterID;
    }

    // Getter and setter for isAnonymousReport
    public boolean isAnonymousReport() {
        return isAnonymousReport;
    }

    public void setAnonymousReport(boolean anonymousReport) {
        isAnonymousReport = anonymousReport;
    }

    // Getter and setter for location
    public Point2D.Double getLocation() {
        return location;
    }

    public void setLocation(Point2D.Double location) {
        this.location = location;
    }

    // Getter and setter for timeReported
    public Date getTimeReported() {
        return timeReported;
    }

    public void setTimeReported(Date timeReported) {
        this.timeReported = timeReported;
    }


}
