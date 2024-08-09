package logic.engine;

import data.transfer.object.LoginDTO;
import data.transfer.object.location.LocationDTO;
import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.NewReportDTO;
import data.transfer.object.report.ReportDTO;
import data.transfer.object.user.NewUserDTO;
import logic.engine.location.history.management.LocationHistoryManager;
import logic.engine.reliability.management.GuardResponse;
import logic.engine.reliability.management.ReliabilityManager;
import logic.engine.report.Comment;
import logic.engine.report.Report;
import logic.engine.report.ReportsManager;
import logic.engine.user.User;
import logic.engine.user.UsersManager;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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
        List<Integer> guardsID = locationHistoryManager.findUsersInRadius(newReportDTO.getDateTime(), newReportDTO.getLatitude(), newReportDTO.getLongitude());
        newReport.setGuards(guardsID);
        reliabilityManager.startReportVerificationProcess(newReport, guardsID);
    }
    public void setGuardVerificationResponse(int verificationProcessID, int guardUserID, GuardResponse response){
        reliabilityManager.setGuardVerificationResponse(verificationProcessID, guardUserID, response);
    }
    public void addOrRemoveLikeToReport(int reportID, int userID){
        reportsManager.addOrRemoveLike(reportID, userID);
    }
    public void addCommentToReport(CommentDTO commentDTO){
        Comment newComment = new Comment(commentDTO.getReportID(), commentDTO.getText(), commentDTO.getWriterUserID(), commentDTO.isAGuardComment());
        reportsManager.addNewComment(newComment);
    }
    public void saveUserLocation(LocationDTO locationDTO){
        if(usersManager.isUserExist(locationDTO.getUserID())){
            locationHistoryManager.saveUserLocation(locationDTO.getUserID(), locationDTO.getDateTime(), locationDTO.getLatitude(), locationDTO.getLongitude());
        }
        else {
            throw new NoSuchElementException("Error - there is no user in the system whose ID number is: "+ locationDTO.getUserID());
        }
    }
}
