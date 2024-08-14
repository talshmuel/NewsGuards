package logic.engine.user;

import data.transfer.object.LoginDTO;
import data.transfer.object.user.NewUserDTO;
import logic.engine.exception.InvalidPasswordException;
import logic.engine.reliability.management.Rate;
import logic.engine.report.Comment;
import newsGuardServer.DatabaseConfig;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public class UsersManager {
    private Map<Integer,User> usersByID;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;

    public UsersManager(){
        usersByID = new HashMap<>();
    }

    public void addNewUser(NewUserDTO newUserData) {
        if (findUserByEmail(newUserData.getEmail()) != null) {
            throw new IllegalArgumentException("User with email " + newUserData.getEmail() + " already exists.");
        }
        User newUser = new User(newUserData,false);
        usersByID.put(newUser.getID(), newUser);
        newUser.pushUserToDB();
    }
    public Integer checkLoginDetailsAndGetUserID(LoginDTO loginDTO){
        User user = findUserByEmail(loginDTO.getEmail());
        if (user == null) {
            user = findAndCreateUserByEmailInDB(loginDTO.getEmail());
            usersByID.put(user.getID(), user); // טעינה מדאטה בייס
            if (user == null) {
                throw new NoSuchElementException("Error - the Email you are trying to log in with does not exist in the system");
            }
        }
        if (user.checkUserPassword(loginDTO.getPasswordToCheck()))
            return user.getID();
        else
            throw new InvalidPasswordException("Incorrect password");
    }
    private User findUserByEmail(String email){
        for (Map.Entry<Integer, User> IDUserPair : usersByID.entrySet()) {
            if (Objects.equals(IDUserPair.getValue().getEmail(), email)) {
                return IDUserPair.getValue();
            }
        }
        return null;
    }

    public static User findAndCreateUserByEmailInDB(String email) {
        String query = "SELECT last_name, first_name, email, country, phone_number, password, reliability_scale, imageurl, location_access_permission " +
                "FROM users WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set the email parameter
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                // Check if result set contains a row
                if (rs.next()) {
                    // Extract user details
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String country = rs.getString("country");
                    String newEmail = rs.getString("email");
                    String password = rs.getString("password");
                    String imageURL = rs.getString("imageurl");
                    String phoneNumber = rs.getString("phone_number");
                    //צריך לעדכן גם את הערך הנכון של הרלביליטי סקייל ושל הid!!!!!!!
                    boolean locationAccessPermission = rs.getBoolean("location_access_permission");

                    // Create NewUserDTO and User objects
                    NewUserDTO newUser = new NewUserDTO(firstName, lastName, country, newEmail, password, imageURL, phoneNumber, locationAccessPermission);
                    return new User(newUser, true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public User findUserByID(int ID) {
        if (isUserExist(ID))
            return usersByID.get(ID);
        else
            return null;
    } //להוסיף שאם לא מצאנו אז לשחזר מהדאטה בייס!!

    public boolean isUserExist(int userID) {
        if (usersByID.containsKey(userID)) {
            return true;
        }

        User user = findAndCreateUserByIDInDB(userID);
        if (user != null)
        {
            usersByID.put(user.getID(), user);
            return true;
        }
        return false;
    }

    public static User findAndCreateUserByIDInDB(int userID) {
        String query = "SELECT last_name, first_name, email, country, phone_number, password, reliability_scale, imageurl, location_access_permission " +
                "FROM users WHERE user_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set the email parameter
            stmt.setInt(1, userID);

            try (ResultSet rs = stmt.executeQuery()) {
                // Check if result set contains a row
                if (rs.next()) {
                    // Extract user details
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String country = rs.getString("country");
                    String newEmail = rs.getString("email");
                    String password = rs.getString("password");
                    String imageURL = rs.getString("imageurl");
                    String phoneNumber = rs.getString("phone_number");
                    //צריך לעדכן גם את הערך הנכון של הרלביליטי סקייל ושל הid!!!!!!!
                    boolean locationAccessPermission = rs.getBoolean("location_access_permission");

                    // Create NewUserDTO and User objects
                    NewUserDTO newUser = new NewUserDTO(firstName, lastName, country, newEmail, password, imageURL, phoneNumber, locationAccessPermission);
                    User user = new User(newUser, true);
                    return user;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

