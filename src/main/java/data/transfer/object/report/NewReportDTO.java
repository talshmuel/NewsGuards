package data.transfer.object.report;

import data.transfer.object.location.LocationDTO;

import javax.xml.stream.Location;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public final class NewReportDTO {
    private final String text;
    private final String imageURL;
    private final int reporterID;
    private final boolean isAnonymousReport;
    private final ArrayList<Integer> IDOfUsersTags;
    private final ArrayList<String> genres;
    private Date dateTime;
    private double latitude;
    private double longitude;

    public NewReportDTO(String text, String imageURL, int reporterID, boolean isAnonymousReport, ArrayList<Integer> IDOfUsersTags, ArrayList<String> genres, Date dateTime, double latitude, double longitude) {
        this.text = text;
        this.imageURL = imageURL;
        this.reporterID = reporterID;
        this.isAnonymousReport = isAnonymousReport;
        this.IDOfUsersTags = IDOfUsersTags;
        this.genres = genres;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dateTime = dateTime;
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

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Date getDateTime() {
        return dateTime;
    }
}
