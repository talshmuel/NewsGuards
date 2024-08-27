package logic.engine.reliability.management;

import logic.engine.report.Report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReliabilityManager {
    private Map<Integer, ReportVerificationProcess> runningVerificationProcesses;

    public ReliabilityManager(){
        runningVerificationProcesses = new HashMap<>();
    }

    public void startReportVerificationProcess(Report reportToVerify, List<Integer> reportGuards){
        runningVerificationProcesses.put(reportToVerify.getID(), new ReportVerificationProcess(reportToVerify, reportGuards));
        //newVerificationProcess.sendVerificationRequestToGuards();
        
    }
    public Map<Integer, Set<Integer>> getGuardsThatNeedToVerify(){
        Map<Integer, Set<Integer>> guardsThatNeedToVerify = new HashMap<>();
        runningVerificationProcesses.forEach((reportID, verificationProcesses)->{
            guardsThatNeedToVerify.put(reportID, verificationProcesses.getGuardsThatNeedToVerify());
        });
        return guardsThatNeedToVerify;

    }
    public void removeGuardThatVerified(int reportID, int guardID){
        runningVerificationProcesses.get(reportID).removeGuardThatVerified(guardID);
    }

}
