package logic.engine.report;

import logic.engine.reliability.management.Rate;
import logic.engine.user.User;

import javax.xml.stream.Location;
import java.sql.Time;
import java.util.ArrayList;

public class Report {
    int ID;
    static int IDGenerator = 0;
    String text;
    String imageURL;
    ArrayList<User> usersWhoLiked;
    ArrayList<Comment> comments;
    ArrayList<User> guards;
    Rate reliabilityRate;
    User reporter;
    boolean isAnonymousReport;
    ArrayList<Integer> IDOfUsersTags;
    ArrayList<Genre> genres;
    Location location;
    Time timeReported;

    public Report(String text, String imageURL, boolean isAnonymousReport, ArrayList<Integer> IDOfUsersTags, ArrayList<Genre> genres, Location location) {
        this.text = text;
        this.imageURL = imageURL;
        this.reporter = reporter;
        this.isAnonymousReport = isAnonymousReport;
        this.IDOfUsersTags = IDOfUsersTags;
        this.genres = genres;
        ID = ++IDGenerator;
    }
    public void setReporter(User reporter){
        this.reporter = reporter;
    }
}
