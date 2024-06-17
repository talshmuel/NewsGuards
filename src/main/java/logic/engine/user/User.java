package logic.engine.user;

import data.transfer.object.user.NewUserDTO;
import logic.engine.notification.Notification;
import logic.engine.reliability.management.Rate;
import logic.engine.report.Genre;
import logic.engine.report.Report;
import logic.engine.user.registration.UserRegistrationDetails;

import java.util.ArrayList;

public class User {
    static int IDGenerator = 0;
    int ID;
    UserRegistrationDetails registrationDetails;
    ArrayList<Report> reports;
    ArrayList<Notification> notifications;
    ArrayList<Report> reportsThatTheUserIsAGuardOf;
    Rate reliabilityRate;

    public User(NewUserDTO newUserData) {
        ID = ++IDGenerator;
        this.registrationDetails = getRegistrationDetails(newUserData);
        reports = new ArrayList<>();
        notifications = new ArrayList<>();
        reportsThatTheUserIsAGuardOf = new ArrayList<>();
        reliabilityRate = Rate.THREE;//todo: להחליט מאיזה דירוג מתחיל יוזר חדש

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
}
