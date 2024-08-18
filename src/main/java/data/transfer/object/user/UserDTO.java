package data.transfer.object.user;

import data.transfer.object.NotificationDTO;
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
    private ArrayList<NotificationDTO> notifications;
    private ArrayList<ReportDTO> IDReportsThatTheUserIsAGuardOf;
    private float reliabilityRate;

    public UserDTO(int ID, String firstName, String lastName, String country, String email, String imageURL, String phoneNumber, boolean locationAccessPermission, ArrayList<ReportDTO> reports, ArrayList<ReportDTO> IDReportsThatTheUserIsAGuardOf, float reliabilityRate) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.email = email;
        this.imageURL = imageURL;
        this.phoneNumber = phoneNumber;
        this.locationAccessPermission = locationAccessPermission;
        this.reports = reports;
        //this.notifications = notifications;
        this.IDReportsThatTheUserIsAGuardOf = IDReportsThatTheUserIsAGuardOf;
        this.reliabilityRate = reliabilityRate;
    }
}
