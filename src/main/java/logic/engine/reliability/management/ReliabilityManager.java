package logic.engine.reliability.management;

import logic.engine.Engine;
import logic.engine.report.Report;
import logic.engine.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReliabilityManager {
    private Map<Integer, ReportVerificationProcess> runningVerificationProcesses;

    public ReliabilityManager(){
        runningVerificationProcesses = new HashMap<>();


    }

    public void startReportVerificationProcess(Report report, List<User> guards){
        Map<User, Verification> guardsVerification = new HashMap<>();
        for (User guard : guards)
            guardsVerification.put(guard, Verification.Pending);
        runningVerificationProcesses.put(report.getID(), new ReportVerificationProcess(report, guardsVerification));
    }

    public void updateGuardVerification(int reportID, int guardID, Verification verification, User guard){
        runningVerificationProcesses.get(reportID).updateGuardVerification(guardID, verification, guard);
    }

    public void restoreVerificationProcess(Map<Integer ,ReportVerificationProcess> reportVerificationProcesses){
        runningVerificationProcesses = reportVerificationProcesses;
    }

}
