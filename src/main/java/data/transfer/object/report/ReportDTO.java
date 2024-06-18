package data.transfer.object.report;

import javax.xml.stream.Location;
import java.sql.Time;
import java.util.ArrayList;

public final class ReportDTO {
    private String text;
    private String imageURL;
    private ArrayList<Integer> IDOfUsersWhoLiked;
    private ArrayList<CommentDTO> comments;
    private ArrayList<Integer> guardsID;
    private Integer reliabilityRate;
    private int reporterID;
    private boolean isAnonymousReport;
    private ArrayList<Integer> IDOfUsersTags;
    private ArrayList<String> genres;
    private Location location;
    private Time timeReported;
}
