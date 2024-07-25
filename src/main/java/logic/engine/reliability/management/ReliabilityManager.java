package logic.engine.reliability.management;

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

    public void startReportVerificationProcess(Report reportToVerify, List<Integer> reportGuards){
        ReportVerificationProcess newVerificationProcess = new ReportVerificationProcess(reportToVerify, reportGuards);
        runningVerificationProcesses.put(newVerificationProcess.getID(), newVerificationProcess);
        //newVerificationProcess.sendVerificationRequestToGuards();
        
    }
    public void getRunningVerificationProcesses(){

    }
    public void setGuardVerificationResponse(int verificationProcessID, Integer guard, GuardResponse response){
        runningVerificationProcesses.get(verificationProcessID).setGuardResponse(guard, response);
    }

}
