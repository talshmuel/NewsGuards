package data.transfer.object.user;

import data.transfer.object.NotificationDTO;

import java.util.ArrayList;

public final class UserDTO {
    int ID;
    String firstName;
    String lastName;
    String country;
    String email;
    String imageURL;
    String phoneNumber;
    ArrayList<String> genrePreference;
    ArrayList<Integer> reliabilityRatePreference;
    ArrayList<String> countriesPreference;
    boolean locationAccessPermission;
    ArrayList<Integer> reportsIDs;
    ArrayList<NotificationDTO> notifications;
    ArrayList<Integer> IDReportsThatTheUserIsAGuardOf;
    int reliabilityRate;
}
