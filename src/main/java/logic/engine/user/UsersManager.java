package logic.engine.user;

import data.transfer.object.LoginDTO;
import data.transfer.object.user.NewUserDTO;
import logic.engine.exception.UserNotFoundException;
import logic.engine.report.Report;

import java.util.Map;
import java.util.Objects;

public class UsersManager {
    Map<Integer,User> usersByID;

    public void addNewUser(NewUserDTO newUserData) {
        for (Map.Entry<Integer, User> IDUserPair : usersByID.entrySet()) {
            if (findUserByEmail(newUserData.getEmail()) != null) {
                throw new IllegalArgumentException("User with email " + newUserData.getEmail() + " already exists.");
            }
        }

        User newUser = new User(newUserData);
        usersByID.put(newUser.getID(), newUser);
    }
    public boolean checkLoginDetails(LoginDTO loginDTO){
        User user = findUserByEmail(loginDTO.getEmail());
        if(user == null){
            throw new UserNotFoundException("Error - the Email you are trying to log in with does not exist in the system");
        }
        return user.checkUserPassword(loginDTO.getPasswordToCheck());
    }
    public void addReportToUser(int userID, Report newReport){
        usersByID.get(userID).addNewReport(newReport);
    }
    public User findUserByEmail(String email){
        for (Map.Entry<Integer, User> IDUserPair : usersByID.entrySet()) {
            if (Objects.equals(IDUserPair.getValue().getEmail(), email)) {
                return IDUserPair.getValue();
            }
        }
        return null;
    }


}
