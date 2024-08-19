package logic.engine.user;

import data.transfer.object.LoginDTO;
import data.transfer.object.user.NewUserDTO;
import data.transfer.object.user.UserDTO;
import logic.engine.exception.InvalidPasswordException;
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
        if(!allFieldsExist(newUserData))
        {
            throw new IllegalArgumentException("Please fill all fields.");
        }
        User newUser = new User(newUserData,3,false);
        usersByID.put(newUser.getID(), newUser);
        newUser.pushUserToDB();
    }
    public Integer checkLoginDetailsAndGetUserID(LoginDTO loginDTO){
        User user = findUserByEmail(loginDTO.getEmail());
        if (user == null) {
            user = findAndRestoreUserByEmailFromDB(loginDTO.getEmail());
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

    public static User findAndRestoreUserByEmailFromDB(String email) {
        String query = "SELECT user_id ,last_name, first_name, email, country, phone_number, password, reliability_scale, imageurl, location_access_permission " +
                "FROM users WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set the email parameter
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                // Check if result set contains a row
                if (rs.next()) {
                    // Extract user details
                    int userID = rs.getInt("user_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String country = rs.getString("country");
                    String newEmail = rs.getString("email");
                    String password = rs.getString("password");
                    String imageURL = rs.getString("imageurl");
                    String phoneNumber = rs.getString("phone_number");
                    int reliabilityScale = rs.getInt("reliability_scale");
                    boolean locationAccessPermission = rs.getBoolean("location_access_permission");

                    // Create NewUserDTO and User objects
                    NewUserDTO newUser = new NewUserDTO(firstName, lastName, country, newEmail, password, imageURL, phoneNumber, locationAccessPermission);
                    User user = new User(newUser, reliabilityScale, true);
                    user.restoreUserID(userID);
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
        if (isUserExistAndRestoreIfFalse(ID))
            return usersByID.get(ID);
        else
            return null;
    }

    public boolean isUserExistAndRestoreIfFalse(int userID) {
        if (usersByID.containsKey(userID)) {
            return true;
        }


        User user = findAndRestoreUserByIDFromDB(userID);
        if (user != null)
        {
            usersByID.put(user.getID(), user);
            return true;
        }
        return false;
    }

    public static User findAndRestoreUserByIDFromDB(int userID) {
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
                    float reliabilityScale = rs.getFloat("reliability_scale");
                    boolean locationAccessPermission = rs.getBoolean("location_access_permission");

                    // Create NewUserDTO and User objects
                    NewUserDTO newUser = new NewUserDTO(firstName, lastName, country, newEmail, password, imageURL, phoneNumber, locationAccessPermission);
                    User user = new User(newUser, reliabilityScale, true);
                    user.restoreUserID(userID);
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

    public boolean allFieldsExist(NewUserDTO newUserData)
    {
        if(newUserData.getEmail() == "" || newUserData.getFirstName() == "" || newUserData.getLastName() == "" ||
                newUserData.getCountry() == "" || newUserData.getPhoneNumber() == "" || newUserData.getPassword() == null)
        {
            return false;
        }
        return true;
    }

    public UserDTO getUserProfile(int userID){
        return usersByID.get(userID).gerUserDTO();
    }

}

