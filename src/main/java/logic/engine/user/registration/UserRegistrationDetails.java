package logic.engine.user.registration;

import logic.engine.reliability.management.Rate;
import logic.engine.report.Genre;

import java.util.ArrayList;
import java.util.Objects;

public class UserRegistrationDetails {
    private String firstName;
    private String lastName;
    private String country;
    private String email;
    private String password;
    private String imageURL;
    private String phoneNumber;
    private UserReportsPreferences reportsPreferences;
    private boolean locationAccessPermission;

    public UserRegistrationDetails(String firstName, String lastName, String country, String email, String password, String imageURL, String phoneNumber, ArrayList<Genre> genrePreference, float reliabilityRatePreference, ArrayList<String> countriesPreference, boolean locationAccessPermission) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.email = email;
        this.password = password;
        this.imageURL = imageURL;
        this.phoneNumber = phoneNumber;
        reportsPreferences = new UserReportsPreferences(genrePreference, reliabilityRatePreference, countriesPreference);
        this.locationAccessPermission = locationAccessPermission;
    }

    public String getEmail() {
        return email;
    }

    public boolean checkUserPassword(String passwordToCheck){
        return Objects.equals(passwordToCheck, this.password);
    }

    public UserReportsPreferences getReportsPreferences() {
        return reportsPreferences;
    }
}
