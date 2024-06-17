package data.transfer.object.user;

import java.util.ArrayList;

public final class NewUserDTO {
    String firstName;
    String lastName;
    String country;
    String email;
    String password;
    String imageURL;
    String phoneNumber;
    ArrayList<String> genrePreference;
    ArrayList<Integer> reliabilityRatePreference;
    ArrayList<String> countriesPreference;
    boolean locationAccessPermission;

    public NewUserDTO(String firstName, String lastName, String country, String email, String password, String imageURL, String phoneNumber, ArrayList<String> genrePreference, ArrayList<Integer> reliabilityRatePreference, ArrayList<String> countriesPreference, boolean locationAccessPermission) {
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

    public String getPassword() {
        return password;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ArrayList<String> getGenrePreference() {
        return genrePreference;
    }

    public ArrayList<Integer> getReliabilityRatePreference() {
        return reliabilityRatePreference;
    }

    public ArrayList<String> getCountriesPreference() {
        return countriesPreference;
    }

    public boolean isLocationAccessPermission() {
        return locationAccessPermission;
    }
}
