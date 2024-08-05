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
    private ArrayList<String> genres;
    private Point2D.Double location;
    private Date timeReported;

    public ReportDTO(String text, String imageURL, ArrayList<Integer> IDOfUsersWhoLiked, ArrayList<CommentDTO> comments, ArrayList<Integer> guardsID, float reliabilityRate, int reporterID, boolean isAnonymousReport, ArrayList<String> genres, Point2D.Double location, Date timeReported) {
        this.text = text;
        this.imageURL = imageURL;
        this.IDOfUsersWhoLiked = IDOfUsersWhoLiked;
        this.comments = comments;
        this.guardsID = guardsID;
        this.reliabilityRate = reliabilityRate;
        this.reporterID = reporterID;
        this.isAnonymousReport = isAnonymousReport;
        this.genres = genres;
        this.location = location;
        this.timeReported = timeReported;
    }
}
