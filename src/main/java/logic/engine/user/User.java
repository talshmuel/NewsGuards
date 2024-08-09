package logic.engine.user;

import data.transfer.object.user.NewUserDTO;
import logic.engine.notification.Notification;
import logic.engine.reliability.management.Rate;
import logic.engine.report.Comment;
import logic.engine.report.Report;
import logic.engine.user.registration.UserRegistrationDetails;

import java.util.*;

public class User {
    static int IDGenerator = 0;
    private int ID;
    private UserRegistrationDetails registrationDetails;
    private ArrayList<Report> reports;
    private ArrayList<Notification> notifications;
    private ArrayList<Report> reportsThatTheUserIsAGuardOf;
    private Rate reliabilityRate;
    private ArrayList<Integer> taggedReports;

    public User(NewUserDTO newUserData) {
        ID = ++IDGenerator;
        this.registrationDetails = getRegistrationDetails(newUserData);
        reports = new ArrayList<>();
        notifications = new ArrayList<>();
        reportsThatTheUserIsAGuardOf = new ArrayList<>();
        reliabilityRate = Rate.THREE;
        taggedReports = new ArrayList<>();
    }
    public String getEmail(){
        return registrationDetails.getEmail();
    }

    public int getID() {
        return ID;
    }

    public UserRegistrationDetails getRegistrationDetails() {
        return registrationDetails;
    }

    private UserRegistrationDetails getRegistrationDetails(NewUserDTO newUserData){

        return new UserRegistrationDetails(newUserData.getFirstName(), newUserData.getLastName(),
                newUserData.getCountry(), newUserData.getEmail(), newUserData.getPassword(),
                newUserData.getImageURL(), newUserData.getPhoneNumber(),
                newUserData.isLocationAccessPermission());

    }
    public boolean checkUserPassword(String passwordToCheck){
        return registrationDetails.checkUserPassword( passwordToCheck);
    }
    public void addNewReport(Report newReport){
        reports.add(newReport);
        newReport.setReporter(this);
    }


}
