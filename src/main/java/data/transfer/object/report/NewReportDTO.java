package data.transfer.object.report;

import javax.xml.stream.Location;
import java.sql.Time;
import java.util.ArrayList;

public final class NewReportDTO {
    String text;
    String imageURL;
    int reporterID;
    boolean isAnonymousReport;
    ArrayList<Integer> IDOfUsersTags;
    ArrayList<String> genres;
    Location location;
    Time timeReported;

    public NewReportDTO(String text, String imageURL, int reporterID, boolean isAnonymousReport, ArrayList<Integer> IDOfUsersTags, ArrayList<String> genres, Location location, Time timeReported) {
        this.text = text;
        this.imageURL = imageURL;
        this.reporterID = reporterID;
        this.isAnonymousReport = isAnonymousReport;
        this.IDOfUsersTags = IDOfUsersTags;
        this.genres = genres;
        this.location = location;
        this.timeReported = timeReported;
    }

    public String getText() {
        return text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getReporterID() {
        return reporterID;
    }

    public boolean isAnonymousReport() {
        return isAnonymousReport;
    }

    public ArrayList<Integer> getIDOfUsersTags() {
        return IDOfUsersTags;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public Location getLocation() {
        return location;
    }

    public Time getTimeReported() {
        return timeReported;
    }
}
