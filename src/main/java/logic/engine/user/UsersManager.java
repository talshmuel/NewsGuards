package logic.engine.user;

import data.transfer.object.LoginDTO;
import data.transfer.object.LoginResponseDTO;
import data.transfer.object.report.ReportDTO;
import data.transfer.object.user.NewUserDTO;
import data.transfer.object.user.UserDTO;
import logic.engine.exception.InvalidPasswordException;
import newsGuardServer.DatabaseConfig;

import java.sql.*;
import java.util.*;

public class UsersManager {
    private Map<Integer,User> usersByID;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;

    public UsersManager(){
        usersByID = new HashMap<>();
    }
    public void addNewUser(NewUserDTO newUserData) {
        if (findUserByEmail(newUserData.getEmail()) != null) {
            throw new IllegalArgumentException("This email already exists.");
        }
        if(!allFieldsExist(newUserData))
        {
            throw new IllegalArgumentException("Please fill all fields.");
        }
        User newUser = new User(newUserData,3,false);
        usersByID.put(newUser.getID(), newUser);
        newUser.pushUserToDB();
    }
    public LoginResponseDTO checkLoginDetailsAndGetUserID(LoginDTO loginDTO){
        User user = findUserByEmail(loginDTO.getEmail());

        if (user == null) {
            user = findAndRestoreUserByEmailFromDB(loginDTO.getEmail());
            usersByID.put(user.getID(), user); // טעינה מדאטה בייס
            if (user == null) {
                throw new NoSuchElementException("Error - the Email you are trying to log in with does not exist in the system");
            }
        }
        if (user.checkUserPassword(loginDTO.getPasswordToCheck()))
            return new LoginResponseDTO("Login successful", user.getID(), user.createFullName());
        else
            throw new InvalidPasswordException("Incorrect password");
    }
    public User findUserByEmail(String email){
        for (Map.Entry<Integer, User> IDUserPair : usersByID.entrySet()) {
            if (Objects.equals(IDUserPair.getValue().getEmail(), email)) {
                return IDUserPair.getValue();
            }
        }
        return findAndRestoreUserByEmailFromDB(email);
    }
    public static User findAndRestoreUserByEmailFromDB(String email) {
        String query = "SELECT user_id, last_name, first_name, country, phone_number, password, reliability_scale, imageurl, location_access_permission " +
                "FROM users WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set the email parameter
            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                // Check if result set contains a row
                if (rs.next()) {
                    // Extract user details
                    int user_id = rs.getInt("user_id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String country = rs.getString("country");
                    String password = rs.getString("password");
                    String imageURL = rs.getString("imageurl");
                    String phoneNumber = rs.getString("phone_number");
                    float reliabilityScale = rs.getFloat("reliability_scale");
                    boolean locationAccessPermission = rs.getBoolean("location_access_permission");

                    // Create NewUserDTO and User objects
                    NewUserDTO newUser = new NewUserDTO(firstName, lastName, country, email, password, imageURL, phoneNumber, locationAccessPermission);
                    User user = new User(newUser, reliabilityScale, true);
                    user.restoreUserID(user_id);
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
    public User findUserByID(int ID) {
        if (isUserExistInLocalOrInDBAndRestore(ID))
            return usersByID.get(ID);
        else
            return null;
    }
    public boolean isUserExistInLocalOrInDBAndRestore(int userID) {
        if (usersByID.containsKey(userID)) {
            System.out.println("checkingggg");
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
                    //user.restoreReportsThatNeedToVerify();
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
    public boolean allFieldsExist(NewUserDTO newUserData){
        System.out.print(newUserData.getEmail());
        System.out.print(newUserData.getPassword());
        System.out.print(newUserData.getCountry());
        System.out.print(newUserData.getFirstName());
        System.out.print(newUserData.getLastName());
        System.out.print(newUserData.getPhoneNumber());

        if(newUserData.getEmail() == "" || newUserData.getFirstName() == "" || newUserData.getLastName() == "" ||
                newUserData.getCountry() == "" || newUserData.getPhoneNumber() == "" || newUserData.getPassword() == null)
        {
            System.out.print("empty");
            return false;
        }
        return true;
    }
    public UserDTO getUserProfile(int userID){
        return usersByID.get(userID).gerUserDTO();
    }
    public ArrayList<ReportDTO> getReportsThatGuardNeedToVerify(int guardID) {
        return usersByID.get(guardID).getReportsThatGuardNeedToVerify();
    }

    public ArrayList<User> getUsersById(ArrayList<Integer> guardsID){
        ArrayList<User> guards = new ArrayList<>();
        User guard;

        for(Integer guardID : guardsID){
            guard = findUserByID(guardID);
            if(guard != null) {
                guards.add(guard);
            }
            else
                throw new NoSuchElementException("Error - there is no user in the system whose ID number is: " + guardID);
        }
        return guards;
    }

    public User getUser(int id)
    {
        return  usersByID.get(id);
    }

}

