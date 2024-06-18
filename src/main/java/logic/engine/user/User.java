package logic.engine.user;

import data.transfer.object.user.NewUserDTO;
import logic.engine.notification.Notification;
import logic.engine.reliability.management.Rate;
import logic.engine.report.Comment;
import logic.engine.report.Genre;
import logic.engine.report.Report;
import logic.engine.user.registration.UserRegistrationDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    static int IDGenerator = 0;
    private int ID;
    private UserRegistrationDetails registrationDetails;
    private ArrayList<Report> reports;
    private ArrayList<Notification> notifications;
    private ArrayList<Report> reportsThatTheUserIsAGuardOf;
    private Rate reliabilityRate;
    private ArrayList<Integer> likedReports;
    private ArrayList<Integer> taggedReports;
    private Map<Integer, Comment> usersCommentsByReportID;

    public User(NewUserDTO newUserData) {
        ID = ++IDGenerator;
        this.registrationDetails = getRegistrationDetails(newUserData);
        reports = new ArrayList<>();
        notifications = new ArrayList<>();
        reportsThatTheUserIsAGuardOf = new ArrayList<>();
        reliabilityRate = Rate.THREE;
        likedReports = new ArrayList<>();
        taggedReports = new ArrayList<>();
        usersCommentsByReportID = new HashMap<>();
    }
    public String getEmail(){
        return registrationDetails.getEmail();
    }

    public int getID() {
        return ID;
    }

    private UserRegistrationDetails getRegistrationDetails(NewUserDTO newUserData){
        ArrayList<Genre> genrePreferences = new ArrayList<>();
        ArrayList<Rate> ratePreferences = new ArrayList<>();

        for(String genreStr : newUserData.getGenrePreference()){
            genrePreferences.add(Genre.convertStringToGenre(genreStr));
        }
        for(Integer reliabilityRateInt : newUserData.getReliabilityRatePreference()){
            ratePreferences.add(Rate.convertIntToRate(reliabilityRateInt));
        }
        return new UserRegistrationDetails(newUserData.getFirstName(), newUserData.getLastName(),
                newUserData.getCountry(), newUserData.getEmail(), newUserData.getPassword(),
                newUserData.getImageURL(), newUserData.getPhoneNumber(), genrePreferences,
                ratePreferences, newUserData.getCountriesPreference(),
                newUserData.isLocationAccessPermission());

    }
    public boolean checkUserPassword(String passwordToCheck){
        return registrationDetails.checkUserPassword( passwordToCheck);
    }
    public void addNewReport(Report newReport){
        reports.add(newReport);
        newReport.setReporter(this);
    }
    public void addOrRemoveLike(int reportID){
        if(likedReports.contains(reportID)){
            likedReports.remove(reportID);
        }
        else {
            likedReports.add(reportID);
        }
    }

    public void addNewComment(Comment comment){
        usersCommentsByReportID.put(comment.getReportID(), comment);
    }
}
