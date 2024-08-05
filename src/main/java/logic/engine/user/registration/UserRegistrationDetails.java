package logic.engine.user.registration;

import org.springframework.jdbc.core.JdbcTemplate;

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
    private boolean locationAccessPermission;
    private JdbcTemplate jdbcTemplate;

    public UserRegistrationDetails(String firstName, String lastName, String country, String email, String password, String imageURL, String phoneNumber, boolean locationAccessPermission) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.email = email;
        this.password = password;
        this.imageURL = imageURL;
        this.phoneNumber = phoneNumber;
        this.locationAccessPermission = locationAccessPermission;
    }

    public String getEmail() {
        return email;
    }

    public boolean checkUserPassword(String passwordToCheck){
        return Objects.equals(passwordToCheck, this.password);
    }
    public void pushUserToDB(int id)
    {
        String sql = "INSERT INTO users (user_id, first_name, last_name, country, email, phone_number, " +
                "password, reliability_scale, image_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, id, firstName, lastName, country,
                email, phoneNumber, password,
                3, imageURL);
    }

}
