package data.transfer.object.user;

import data.transfer.object.NotificationDTO;

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
    private ArrayList<Integer> reportsIDs;
    private ArrayList<NotificationDTO> notifications;
    private ArrayList<Integer> IDReportsThatTheUserIsAGuardOf;
    private int reliabilityRate;
}
