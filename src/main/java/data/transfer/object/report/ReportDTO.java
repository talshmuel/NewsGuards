package data.transfer.object.report;

import javax.xml.stream.Location;
import java.sql.Time;
import java.util.ArrayList;

public final class ReportDTO {
    String text;
    String imageURL;
    ArrayList<Integer> IDOfUsersWhoLiked;
    ArrayList<CommentDTO> comments;
    ArrayList<Integer> guardsID;
    Integer reliabilityRate;
    int reporterID;
    boolean isAnonymousReport;
    ArrayList<Integer> IDOfUsersTags;
    ArrayList<String> genres;
    Location location;
    Time timeReported;
}
