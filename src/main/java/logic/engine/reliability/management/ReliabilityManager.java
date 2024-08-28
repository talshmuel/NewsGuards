package logic.engine.reliability.management;

import logic.engine.Engine;
import logic.engine.report.Report;
import logic.engine.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReliabilityManager {
    private Map<Integer, ReportVerificationProcess> runningVerificationProcesses;

    public ReliabilityManager(){
        runningVerificationProcesses = new HashMap<>();

    }

    public void startReportVerificationProcess(Report report, List<User> guards){
        runningVerificationProcesses.put(report.getID(), new ReportVerificationProcess(report, guards));
    }

    public void updateGuardVerification(int reportID, int guardID, Verification verification){
        runningVerificationProcesses.get(reportID).updateGuardVerification(guardID, verification);
    }

}
