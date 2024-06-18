package logic.engine;

import data.transfer.object.LoginDTO;
import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.NewReportDTO;
import data.transfer.object.user.NewUserDTO;
import logic.engine.location.history.management.LocationHistoryManager;
import logic.engine.reliability.management.GuardResponse;
import logic.engine.reliability.management.ReliabilityManager;
import logic.engine.report.Comment;
import logic.engine.report.Report;
import logic.engine.report.ReportsManager;
import logic.engine.user.User;
import logic.engine.user.UsersManager;

public class Engine {
    private UsersManager usersManager;
    private ReportsManager reportsManager;
    private LocationHistoryManager locationHistoryManager;
    private ReliabilityManager reliabilityManager;

    public Engine(){
        usersManager = new UsersManager();
        reportsManager = new ReportsManager();
        locationHistoryManager = new LocationHistoryManager();
        reliabilityManager = new ReliabilityManager();
    }

    public void createNewUser(NewUserDTO newUserData){
        usersManager.addNewUser(newUserData);
    }
    public boolean checkLoginDetails(LoginDTO loginDTO){
        return usersManager.checkLoginDetails(loginDTO);
    }
    public void addNewReportAndStartVerificationProcess(NewReportDTO newReportDTO){
        User reporter = usersManager.findUserByID(newReportDTO.getReporterID());
        Report newReport = reportsManager.addNewReport(newReportDTO, reporter);
        reliabilityManager.startReportVerificationProcess(newReport);
    }
    public void setGuardVerificationResponse(int verificationProcessID, int guardUserID, GuardResponse response){
        User guard = usersManager.findUserByID(guardUserID);
        reliabilityManager.setGuardVerificationResponse(verificationProcessID, guard, response);
    }
    public void addOrRemoveLikeToReport(int reportID, int userID){
        reportsManager.addOrRemoveLike(reportID, userID);
        usersManager.addOrRemoveLike(userID, reportID);
    }
    public void addCommentToReport(CommentDTO commentDTO){
        Comment newComment = new Comment(commentDTO.getReportID(), commentDTO.getText(), commentDTO.getWriterUserID(), commentDTO.isAGuardComment());
        reportsManager.addNewComment(newComment);
        usersManager.addNewComment(newComment);
    }
}
