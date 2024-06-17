package logic.engine;

import data.transfer.object.LoginDTO;
import data.transfer.object.report.NewReportDTO;
import data.transfer.object.user.NewUserDTO;
import logic.engine.location.history.management.LocationHistoryManager;
import logic.engine.reliability.management.GuardResponse;
import logic.engine.reliability.management.ReliabilityManager;
import logic.engine.report.Report;
import logic.engine.report.ReportsManager;
import logic.engine.user.User;
import logic.engine.user.UsersManager;
import org.springframework.stereotype.Component;

@Component
public class Engine {
    UsersManager usersManager;
    ReportsManager reportsManager;
    LocationHistoryManager locationHistoryManager;
    ReliabilityManager reliabilityManager;

    public void createNewUser(NewUserDTO newUserData){
        usersManager.addNewUser(newUserData);

    }
    public boolean checkLoginDetails(LoginDTO loginDTO){
        return usersManager.checkLoginDetails(loginDTO);
    }
    public void addNewReportAndStartVerificationProcess(NewReportDTO newReportDTO){
        Report newReport = reportsManager.addNewReport(newReportDTO);

        usersManager.addReportToUser(newReportDTO.getReporterID(), newReport);
        reliabilityManager.startReportVerificationProcess(newReport);
    }
    public void setGuardVerificationResponse(int verificationProcessID, User guard, GuardResponse response){

    }
}
