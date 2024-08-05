package logic.engine.report;

import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.ReportDTO;
import logic.engine.location.history.management.GeocodingService;
import logic.engine.location.history.management.NominatimExample;
import logic.engine.reliability.management.Rate;
import logic.engine.user.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;

public class Report {
    private final int ID;
    private static int IDGenerator = 0;
    private String text;
    private String imageURL;
    private final Set<Integer> usersWhoLiked;
    private final ArrayList<Comment> comments;
    private List<Integer> guards;
    private float reliabilityRate;
    private User reporter;
    private boolean isAnonymousReport;
    private Point2D.Double location;
    private Date timeReported;
    private JdbcTemplate jdbcTemplate;

    public Report(String text, String imageURL, User reporter, boolean isAnonymousReport, Point2D.Double location, Date timeReported) {
        this.text = text;
        this.imageURL = imageURL;
        this.usersWhoLiked = new HashSet<>();
        this.comments = new ArrayList<>();
        this.reporter = reporter;
        this.isAnonymousReport = isAnonymousReport;
        this.location = location;
        this.timeReported = timeReported;
        ID = ++IDGenerator;
        reporter.addNewReport(this);

    }
    public int getID() {
        return ID;
    }

    public void setReporter(User reporter){
        this.reporter = reporter;
    }
    public void addOrRemoveLike(int userID){
        if(usersWhoLiked.contains(userID)){
            usersWhoLiked.remove(userID);
        }
        else {
            usersWhoLiked.add(userID);
        }
    }

    public void addNewComment(Comment comment){
        comments.add(comment);
    }

    public void setGuards(List<Integer> guards) {
        this.guards = guards;
    }

    public String getCountry() throws Exception {
        //GeocodingService geocodingService = new GeocodingService();
        //String country = geocodingService.getCountry(location);

        String country = NominatimExample.getCountry(location);

        if (country == null) {
            throw new NoSuchElementException("Country not found for the provided location");
        }
        return country;
    }

    public float getReliabilityRate() {
        return reliabilityRate;
    }
    public ReportDTO getReportDTO(){
        ArrayList<CommentDTO> commentsDTO = new ArrayList<>();

        for (Comment comment : comments){
            commentsDTO.add(comment.getCommentDTO());
        }

        return new ReportDTO(text, imageURL, new ArrayList<>(usersWhoLiked), commentsDTO, new ArrayList<>(guards),
                reliabilityRate, reporter.getID(), isAnonymousReport,
                location, timeReported);
    }
    public void pushReportToDB(int reporter_id)
    {
        String sql = "INSERT INTO reports (report_id, text, user_id, likes_number, report_rate, image_url) VALUES (?, ?, ?, ?, ?, ?)";//

        jdbcTemplate.update(sql, ID, text, reporter_id, usersWhoLiked.size(), reliabilityRate, imageURL);
    } //צריך לאתחל את report_rate

}
