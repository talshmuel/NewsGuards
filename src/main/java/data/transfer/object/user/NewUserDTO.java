package data.transfer.object.user;

public final class NewUserDTO {
    private final String firstName;
    private final String lastName;
    private final String country;
    private final String email;
    private final String password;
    private final String imageURL;
    private final String phoneNumber;
    private final boolean locationAccessPermission;

    public NewUserDTO(String firstName, String lastName, String country, String email, String password, String imageURL, String phoneNumber, boolean locationAccessPermission) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.email = email;
        this.password = password;
        this.imageURL = imageURL;
        this.phoneNumber = phoneNumber;
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
    public boolean isLocationAccessPermission() {
        return locationAccessPermission;
    }
}
