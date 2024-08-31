package data.transfer.object.report;

import data.transfer.object.location.LocationDTO;

import javax.xml.stream.Location;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public final class NewReportDTO {//
    private final String text;
    private final String imageURL;
    private final int reporterID;
    private final boolean isAnonymousReport;
    private LocalDateTime timeReported; // Ensure this is LocalDateTime
    private double latitude;
    private double longitude;

    public NewReportDTO(String text, String imageURL, int reporterID, boolean isAnonymousReport, LocalDateTime timeReported, double latitude, double longitude) {
        this.text = text;
        this.imageURL = imageURL;
        this.reporterID = reporterID;
        this.isAnonymousReport = isAnonymousReport;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public LocalDateTime getTimeReported() {
        return timeReported;
    }

}
