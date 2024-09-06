package logic.engine;

import data.transfer.object.LoginDTO;
import data.transfer.object.LoginResponseDTO;
import data.transfer.object.location.LocationDTO;
import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.NewReportDTO;
import data.transfer.object.report.ReportDTO;
import data.transfer.object.user.NewUserDTO;
import data.transfer.object.user.UserDTO;
import logic.engine.location.history.management.LocationHistoryManager;
import logic.engine.reliability.management.ReportVerificationProcess;
import logic.engine.reliability.management.Verification;
import logic.engine.reliability.management.ReliabilityManager;
import logic.engine.report.Comment;
import logic.engine.report.Report;
import logic.engine.report.ReportsManager;
import logic.engine.user.User;
import logic.engine.user.UsersManager;
import newsGuardServer.DatabaseConfig;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

@Service
public class Engine {
    public static int staticLoginUserId;
    private final UsersManager usersManager;
    private final ReportsManager reportsManager;
    private LocationHistoryManager locationHistoryManager;
    private final ReliabilityManager reliabilityManager;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;

    public Engine(){
        usersManager = new UsersManager();
        reportsManager = new ReportsManager();
        locationHistoryManager = new LocationHistoryManager();
        reliabilityManager = new ReliabilityManager();
        restoreReportsInVerificationProcessFromDB();
    }
    public void createNewUser(NewUserDTO newUserData){
        usersManager.addNewUser(newUserData);
    }
    public LoginResponseDTO checkLoginDetails(LoginDTO loginDTO){
        LoginResponseDTO loginResponseDTO= usersManager.checkLoginDetailsAndGetUserID(loginDTO);
        restoreReportsThatNeedToVerify(loginDTO.getEmail());
        return loginResponseDTO;
    }
    public void addNewReportAndStartVerificationProcess(NewReportDTO newReportDTO){
        User reporter = usersManager.findUserByID(newReportDTO.getReporterID());
        if(reporter == null){
            throw new NoSuchElementException("Error - there is no user in the system whose ID number is: "+ newReportDTO.getReporterID());
        }
        Report newReport = reportsManager.addNewReport(newReportDTO, reporter);
        ArrayList<Integer> guardsID = locationHistoryManager.findUsersInRadius(newReportDTO.getReporterID(), newReportDTO.getLatitude(), newReportDTO.getLongitude());
        newReport.setGuards(guardsID);
        newReport.storeGuardsInDB(guardsID);
        ArrayList<User> guards = usersManager.getUsersById(guardsID);
        reliabilityManager.startReportVerificationProcess(newReport, guards);
    }
    public void addOrRemoveLikeToReport(int reportID, int userID){
        reportsManager.addOrRemoveLike(reportID, userID);
    }
    public void addCommentToReport(CommentDTO commentDTO){
        User commenter = usersManager.findUserByID(commentDTO.getCommenterUserID());
        if(commenter == null){
            throw new NoSuchElementException("Error - there is no user in the system whose ID number is: "+ commenter.getID());
        }
        Comment newComment = new Comment(commentDTO.getReportID(), commentDTO.getText(), commentDTO.getCommenterUserID(), commentDTO.isAGuardComment(), false, 0, commenter.createFullName());
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

        ArrayList<ReportDTO> reportsThatUserGuardDTOS = new ArrayList<>();
        for(int reportIDUserGuardOf : user.getReportsThatTheUserIsAGuardOf().keySet()) {
            reportsThatUserGuardDTOS.add(reportsManager.getReports().get(reportIDUserGuardOf).getReportDTO());
        }
        return user.gerUserDTO(reportsThatUserGuardDTOS,reportsManager.getReports());
    }
    public ArrayList<ReportDTO> getLastTwentyReportsToHomePage(){
        return reportsManager.getLastTwentyReportsToHomePage();
    }
    public ArrayList<ReportDTO> getReportsThatGuardNeedToVerify(int guardID){
        if(!usersManager.isUserExistInLocalOrInDBAndRestore(guardID))
            throw new IllegalArgumentException("Error - there is no user in the system whose ID number is: "+ guardID);
        return usersManager.getReportsThatGuardNeedToVerify(guardID);
    }
    public void updateGuardVerification(int reportID, int guardID, int guardVerification){
        Verification verificationEnum = Verification.fromInt(guardVerification);

        if(!usersManager.isUserExistInLocalOrInDBAndRestore(guardID)){
            throw new NoSuchElementException("Error - there is no user in the system whose ID number is: "+ guardID);
        }
        if(reportsManager.findAndRestoreReportFromDB(reportID) == null){
            throw new NoSuchElementException("Error - there is no report in the system whose ID number is: " + reportID);
        }
        //usersManager.getUser(guardID);
        reliabilityManager.updateGuardVerification(reportID, guardID, verificationEnum,usersManager.getUser(guardID));
    }

    public void restoreReportsInVerificationProcessFromDB()
    {
        ArrayList<Integer> reportsInVerificationProcess = new ArrayList<>();
        String query = "SELECT report_id FROM reports_verification_process";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                // Loop through each row in the result set
                while (rs.next()) {
                    int reportID = rs.getInt("report_id");
                    reportsInVerificationProcess.add(reportID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log or handle exceptions as needed
        }
        restoreVerificationProcessInReliabilityManager(reportsInVerificationProcess);
    }
    private void restoreVerificationProcessInReliabilityManager(ArrayList<Integer> reportsID){
        Map<Integer ,ReportVerificationProcess> reportVerificationProcesses = new HashMap();
        for (Integer reportID : reportsID){

            Map<User, Verification> guardsVerification = new HashMap<>();
            Report report = reportsManager.findAndRestoreReportFromDB(reportID);
            report.getGuardsVerifications().forEach((guardID, verification)->{
                User guard = usersManager.findUserByID(guardID);
                guardsVerification.put(guard, verification);

            });
            reportVerificationProcesses.put(reportID ,new ReportVerificationProcess(report, guardsVerification));

        }
        reliabilityManager.restoreVerificationProcess(reportVerificationProcesses);

    }

    public void restoreReportsThatNeedToVerify(String loggedInUserEmail)
    {
        User loggedInUser = usersManager.findUserByEmail(loggedInUserEmail);
        String query = "SELECT report_id FROM guards_verification WHERE user_id = ? AND user_response = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, loggedInUser.getID());
            stmt.setInt(2, (Verification.Pending).toInt());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int reportID = rs.getInt("report_id");
                    Report report = reportsManager.findAndRestoreReportFromDB(reportID);
                    loggedInUser.setReportsThatNeedToVerify(reportID, report);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions (e.g., log errors)
        }
    }

//    public Verification convertIntToGuardVerificationEnum(int guardVerificationInt){
//        switch (guardVerificationInt){
//            case 1:
//                return Verification.Approve;
//            case 2:
//                return Verification.Deny;
//            case 3:
//                return Verification.Avoid;
//            default:
//                throw new IllegalArgumentException("Guard verification should be number 1-3");
//        }
//    }

}