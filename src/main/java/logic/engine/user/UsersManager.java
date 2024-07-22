package logic.engine.user;

import data.transfer.object.LoginDTO;
import data.transfer.object.user.NewUserDTO;
import logic.engine.exception.UserNotFoundException;
import logic.engine.report.Comment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UsersManager {
    private Map<Integer,User> usersByID = new HashMap<>();

    public void addNewUser(NewUserDTO newUserData) throws SQLException {
        for (Map.Entry<Integer, User> IDUserPair : usersByID.entrySet()) {
            if (findUserByEmail(newUserData.getEmail()) != null) {
                throw new IllegalArgumentException("User with email " + newUserData.getEmail() + " already exists.");
            }
        }

        User newUser = new User(newUserData);
        usersByID.put(newUser.getID(), newUser);
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5433/newsguardproject", "postgres", "4Xkp61jk!4Xkp61jk!");

            String sql = "INSERT INTO users (first_name, last_name, country, email, password, imageURL, phone_number) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";.
            stmt = conn.prepareStatement(sql);

            // Set parameters for the PreparedStatement
            stmt.setString(1, newUserData.getFirstName());     // Replace with appropriate getter methods
            stmt.setString(2, newUserData.getLastName());      // Replace with appropriate getter methods
            stmt.setString(3, newUserData.getCountry());       // Replace with appropriate getter methods
            stmt.setString(4, newUserData.getEmail());         // Replace with appropriate getter methods
            stmt.setString(5, newUserData.getPassword());      // Replace with appropriate getter methods
            stmt.setString(6, newUserData.getImageURL());      // Replace with appropriate getter methods
            stmt.setString(7, newUserData.getPhoneNumber());   // Replace with appropriate getter methods

            // Execute the statement
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting user data!", e);
        } finally {

            if (conn != null) {
                conn.close();
            }
        }
    }
    public boolean checkLoginDetails(LoginDTO loginDTO){
        User user = findUserByEmail(loginDTO.getEmail());
        if(user == null){
            throw new UserNotFoundException("Error - the Email you are trying to log in with does not exist in the system");
        }
        return user.checkUserPassword(loginDTO.getPasswordToCheck());
    }
    public User findUserByEmail(String email){
        for (Map.Entry<Integer, User> IDUserPair : usersByID.entrySet()) {
            if (Objects.equals(IDUserPair.getValue().getEmail(), email)) {
                return IDUserPair.getValue();
            }
        }
        return null;
    }
    public User findUserByID(int ID){
        return usersByID.get(ID);
    }

    public void addOrRemoveLike(int userID, int reportID){
        usersByID.get(userID).addOrRemoveLike(reportID);
    }
    public void addNewComment(Comment comment){
        usersByID.get(comment.getWriterID());
    }

}
