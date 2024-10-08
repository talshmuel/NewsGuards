package data.transfer.object.user;

import data.transfer.object.report.ReportDTO;

import java.util.ArrayList;

public final class UserDTO {
    private int ID;
    private String firstName;
    private String lastName;
    private String country;
    private String email;
    private String imageURL;
    private String phoneNumber;
    private boolean locationAccessPermission;
    private ArrayList<ReportDTO> reports;
    private ArrayList<ReportDTO> reportsThatTheUserIsAGuardOf = new ArrayList<>();
    private float reliabilityRate;

    public UserDTO(int ID, String firstName, String lastName, String country, String email, String imageURL, String phoneNumber, boolean locationAccessPermission, ArrayList<ReportDTO> reports,ArrayList<ReportDTO> reportsThatTheUserIsAGuardOf, float reliabilityRate) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.email = email;
        this.imageURL = imageURL;
        this.phoneNumber = phoneNumber;
        this.locationAccessPermission = locationAccessPermission;
        this.reports = reports;
        this.reportsThatTheUserIsAGuardOf = reportsThatTheUserIsAGuardOf;
        //this.notifications = notifications;
        //this.IDReportsThatTheUserIsAGuardOf = IDReportsThatTheUserIsAGuardOf;
        this.reliabilityRate = reliabilityRate;
    }

    public int getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isLocationAccessPermission() {
        return locationAccessPermission;
    }

    public ArrayList<ReportDTO> getReports() {
        return reports;
    }

    public ArrayList<ReportDTO> getReportsThatTheUserIsAGuardOf() {
        return reportsThatTheUserIsAGuardOf;
    }

    public float getReliabilityRate() {
        return reliabilityRate;
    }
}
