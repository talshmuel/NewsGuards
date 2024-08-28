package logic.engine.reliability.management;

import logic.engine.report.Report;
import logic.engine.user.User;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReportVerificationProcess {
    int timeWindowToVerify = 1; //todo change, it is just a try
    TimeUnit units = TimeUnit.MINUTES;//todo change, it is just a try
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private boolean isRunning = true;
    private Report report;
    private Map<Integer, GuardVerification> guardsVerification;
    private float reliabilityRate;
    public ReportVerificationProcess(Report report, List<User> guards){
        this.report = report;
        guardsVerification = new HashMap<>();
        for(User guard : guards){
            guardsVerification.put(guard.getID(), new GuardVerification(guard, Verification.Pending));
            guard.addReportToVerify(report);
        }
        scheduler.schedule(this::stopWindowToVerify, timeWindowToVerify, units);
    }
    public void updateGuardVerification(int guardID, Verification verification){
        report.updateGuardVerification(guardID, verification);
        GuardVerification guardVerification = guardsVerification.get(guardID);
        guardVerification.setVerification(verification);
        guardVerification.getGuard().updateGuardVerification(report.getID(), verification);
    }
    private void stopWindowToVerify() {
        isRunning = false;
        scheduler.shutdown();

        for(GuardVerification guardVerification : guardsVerification.values()){
            guardVerification.getGuard().removeReportToVerify(report.getID());
        }
        calculateReportReliabilityRate();
    }

    public void calculateReportReliabilityRate(){
        float countApprove = 0, countDeny = 0;
        for(GuardVerification guardVerification : guardsVerification.values()){
            if(guardVerification.getVerification() == Verification.Approve)
                countApprove++;
            else if (guardVerification.getVerification() == Verification.Deny)
                countDeny++;
        }
        reliabilityRate = (countApprove * 5)/(countApprove+countDeny);
        report.setReliabilityRate(reliabilityRate);
    }
}
