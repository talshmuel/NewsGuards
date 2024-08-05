package data.transfer.object.user;

import java.util.ArrayList;

public final class NewUserDTO {
    private final String firstName;
    private final String lastName;
    private final String country;
    private final String email;
    private final String password;
    private final String imageURL;
    private final String phoneNumber;
    private final ArrayList<String> genrePreference;
    private final float reliabilityRatePreference;
    private final ArrayList<String> countriesPreference;
    private final boolean locationAccessPermission;

    public NewUserDTO(String firstName, String lastName, String country, String email, String password, String imageURL, String phoneNumber, ArrayList<String> genrePreference, float reliabilityRatePreference, ArrayList<String> countriesPreference, boolean locationAccessPermission) {
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

    public float getReliabilityRatePreference() {
        return reliabilityRatePreference;
    }

    public ArrayList<String> getCountriesPreference() {
        return countriesPreference;
    }

    public boolean isLocationAccessPermission() {
        return locationAccessPermission;
    }
}
