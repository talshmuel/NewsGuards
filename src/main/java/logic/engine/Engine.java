package logic.engine;

import data.transfer.object.LoginDTO;
import data.transfer.object.location.LocationDTO;
import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.NewReportDTO;
import data.transfer.object.report.ReportDTO;
import data.transfer.object.user.NewUserDTO;
import data.transfer.object.user.UserDTO;
import logic.engine.location.history.management.LocationHistoryManager;
import logic.engine.reliability.management.Verification;
import logic.engine.reliability.management.ReliabilityManager;
import logic.engine.report.Comment;
import logic.engine.report.Report;
import logic.engine.report.ReportsManager;
import logic.engine.user.User;
import logic.engine.user.UsersManager;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Engine {
    private final UsersManager usersManager;
    private final ReportsManager reportsManager;
    private LocationHistoryManager locationHistoryManager;
    private final ReliabilityManager reliabilityManager;

    public Engine(){
        usersManager = new UsersManager();
        reportsManager = new ReportsManager();
        locationHistoryManager = new LocationHistoryManager();
        reliabilityManager = new ReliabilityManager();
    }
    public void createNewUser(NewUserDTO newUserData){
        usersManager.addNewUser(newUserData);
    }
    public Integer checkLoginDetails(LoginDTO loginDTO){
        return usersManager.checkLoginDetailsAndGetUserID(loginDTO);
    }
    public void addNewReportAndStartVerificationProcess(NewReportDTO newReportDTO){
        User reporter = usersManager.findUserByID(newReportDTO.getReporterID());
        if(reporter == null){
            throw new NoSuchElementException("Error - there is no user in the system whose ID number is: "+ newReportDTO.getReporterID());
        }
        Report newReport = reportsManager.addNewReport(newReportDTO, reporter);
        ArrayList<Integer> guardsID = locationHistoryManager.findUsersInRadius(newReportDTO.getReporterID(), newReportDTO.getLatitude(), newReportDTO.getLongitude());
        newReport.setGuards(guardsID);
        ArrayList<User> guards = usersManager.getUsersById(guardsID);
        reliabilityManager.startReportVerificationProcess(newReport, guards);
    }
    public void addOrRemoveLikeToReport(int reportID, int userID){
        reportsManager.addOrRemoveLike(reportID, userID);
    }
    public void addCommentToReport(CommentDTO commentDTO){
        Comment newComment = new Comment(commentDTO.getReportID(), commentDTO.getText(), commentDTO.getWriterUserID(), commentDTO.isAGuardComment(), false, 0);
        reportsManager.addNewComment(newComment);
    }
    public void saveUserLocation(LocationDTO locationDTO){
        if(usersManager.isUserExistInLocalOrInDBAndRestore(locationDTO.getUserID())){
            locationHistoryManager.saveUserLocation(locationDTO.getUserID(), locationDTO.getLatitude(), locationDTO.getLongitude());
        }
        else {
            throw new NoSuchElementException("Error - there is no user in the system whose ID number is: "+ locationDTO.getUserID());
        }
    }

    public UserDTO getUserProfile(int userID){
        User user = usersManager.findUserByID(userID);
        if(user == null){
            throw new NoSuchElementException("Error - there is no user in the system whose ID number is: "+ userID);
        }
        return user.gerUserDTO();
    }
    public ArrayList<ReportDTO> getLastTwentyReportsToHomePage()    {
        return reportsManager.getLastTwentyReportsToHomePage();
    }
    public ArrayList<ReportDTO> getReportThatGuardNeedToVerify(int guardID){
        if(!usersManager.isUserExistInLocalOrInDBAndRestore(guardID))
            throw new IllegalArgumentException("Error - there is no user in the system whose ID number is: "+ guardID);
        return usersManager.getReportsThatGuardNeedToVerify(guardID);
    }
    public void updateGuardVerification(int reportID, int guardID, int guardVerification){
        Verification verificationEnum = convertIntToGuardVerificationEnum(guardVerification);

        if(!usersManager.isUserExistInLocalOrInDBAndRestore(guardID)){
            throw new NoSuchElementException("Error - there is no user in the system whose ID number is: "+ guardID);
        }
        if(reportsManager.findAndRestoreReportFromDB(reportID) == null){
            throw new NoSuchElementException("Error - there is no report in the system whose ID number is: " + reportID);
        }
        reliabilityManager.updateGuardVerification(reportID, guardID, verificationEnum);
    }
    public Verification convertIntToGuardVerificationEnum(int guardVerificationInt){
        switch (guardVerificationInt){
            case 1:
                return Verification.Approve;
            case 2:
                return Verification.Deny;
            case 3:
                return Verification.Avoid;
            default:
                throw new IllegalArgumentException("Guard verification should be number 1-3");
        }
    }

}