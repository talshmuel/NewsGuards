package logic.engine.report;

import data.transfer.object.report.CommentDTO;
import logic.engine.reliability.management.Rate;
import logic.engine.user.User;

import javax.xml.stream.Location;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Report {
    private int ID;
    private static int IDGenerator = 0;
    private String text;
    private String imageURL;
    private Set<Integer> usersWhoLiked;
    private ArrayList<Comment> comments;
    private ArrayList<User> guards;
    private Rate reliabilityRate;
    private User reporter;
    private boolean isAnonymousReport;
    private ArrayList<Integer> IDOfUsersTags;
    private ArrayList<Genre> genres;
    private Location location;
    private Time timeReported;

    public Report(String text, String imageURL, User reporter, boolean isAnonymousReport, ArrayList<Integer> IDOfUsersTags, ArrayList<Genre> genres, Location location, Time timeReported) {
        this.text = text;
        this.imageURL = imageURL;
        this.usersWhoLiked = new HashSet<>();
        this.comments = new ArrayList<>();
        //this.guards = guards;//todo find where to set it
        this.reporter = reporter;
        this.isAnonymousReport = isAnonymousReport;
        this.IDOfUsersTags = IDOfUsersTags;
        this.genres = genres;
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

}
