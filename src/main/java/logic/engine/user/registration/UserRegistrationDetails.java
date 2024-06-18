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
    private ArrayList<Genre> genrePreference;
    private ArrayList<Rate> reliabilityRatePreference;
    private ArrayList<String> countriesPreference;
    private boolean locationAccessPermission;

    public UserRegistrationDetails(String firstName, String lastName, String country, String email, String password, String imageURL, String phoneNumber, ArrayList<Genre> genrePreference, ArrayList<Rate> reliabilityRatePreference, ArrayList<String> countriesPreference, boolean locationAccessPermission) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.email = email;
        this.password = password;
        this.imageURL = imageURL;
        this.phoneNumber = phoneNumber;
        this.genrePreference = genrePreference;
        this.reliabilityRatePreference = reliabilityRatePreference;
        this.countriesPreference = countriesPreference;
        this.locationAccessPermission = locationAccessPermission;
    }

    public String getEmail() {
        return email;
    }

    public boolean checkUserPassword(String passwordToCheck){
        return Objects.equals(passwordToCheck, this.password);
    }
}
