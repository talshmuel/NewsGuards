package logic.engine.user;

import data.transfer.object.LoginDTO;
import data.transfer.object.user.NewUserDTO;
import logic.engine.exception.InvalidPasswordException;
import logic.engine.report.Comment;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

public class UsersManager {
    private Map<Integer,User> usersByID;

    public UsersManager(){
        usersByID = new HashMap<>();
    }

    public void addNewUser(NewUserDTO newUserData) {
        if (findUserByEmail(newUserData.getEmail()) != null) {
            throw new IllegalArgumentException("User with email " + newUserData.getEmail() + " already exists.");
        }
        User newUser = new User(newUserData);
        usersByID.put(newUser.getID(), newUser);
        newUser.getRegistrationDetails().pushUserToDB(newUser.getID());
    }
    public Integer checkLoginDetailsAndGetUserID(LoginDTO loginDTO){
        User user = findUserByEmail(loginDTO.getEmail());
        if(user == null){
            throw new NoSuchElementException("Error - the Email you are trying to log in with does not exist in the system");
        }
        if(user.checkUserPassword(loginDTO.getPasswordToCheck()))
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
    public User findUserByID(int ID){
        return usersByID.get(ID);
    } //להוסיף שאם לא מצאנו אז לשחזר מהדאטה בייס!!

    public boolean isUserExist(int userID){
        return usersByID.containsKey(userID);
    }


}
