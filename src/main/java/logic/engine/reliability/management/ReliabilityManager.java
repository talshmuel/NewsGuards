package logic.engine.reliability.management;

import logic.engine.Engine;
import logic.engine.report.Report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReliabilityManager {
    Engine engine;
    private Map<Integer, ReportVerificationProcess> runningVerificationProcesses;

    public ReliabilityManager(Engine engine){
        runningVerificationProcesses = new HashMap<>();
        this.engine = engine;
    }

    public void startReportVerificationProcess(int reportIDToVerify, List<Integer> reportGuards){
        runningVerificationProcesses.put(reportIDToVerify, new ReportVerificationProcess(reportIDToVerify, reportGuards, engine));
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
