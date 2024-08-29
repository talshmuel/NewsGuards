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
    float maxNumberOfReliabilityStars = 5;
    float minNumberOfReliabilityStars = 0;
    float guardsRatingDecrease = 0.2f;
    float guardRatingIncrease = 0.1f;

    float reporterRatingDecrease = 1f;
    float reporterRatingIncrease = 0.1f;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private boolean isRunning = true;
    private Report report;
    private Map<Integer, GuardVerification> guardsVerification;
    private float reportReliabilityRate;
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

    public void calculateReliabilityRate(){
        calculateReportReliabilityRate();//report
        calculateGuardsReliabilityRate();//guards
        calculateReporterReliabilityRate();//reporter
    }
    public void calculateReportReliabilityRate(){
        float countApprove = 0, countDeny = 0;
        for(GuardVerification guardVerification : guardsVerification.values()){
            if(guardVerification.getVerification() == Verification.Approve)
                countApprove++;
            else if (guardVerification.getVerification() == Verification.Deny)
                countDeny++;
        }
        reportReliabilityRate = (countApprove * maxNumberOfReliabilityStars)/(countApprove+countDeny);
        report.setReliabilityRate(reportReliabilityRate);
    }
    public void calculateGuardsReliabilityRate(){
        for(GuardVerification guardsVerification : guardsVerification.values()){
            Verification verification = guardsVerification.getVerification();
            User guard = guardsVerification.getGuard();
            float currentGuardReliabilityRate = guard.getReliabilityRate();

            if(reportReliabilityRate <= 1 && verification == Verification.Approve){
                if(currentGuardReliabilityRate - guardsRatingDecrease <=minNumberOfReliabilityStars)
                    guard.setReliabilityRate(minNumberOfReliabilityStars);
                else
                    guard.setReliabilityRate(currentGuardReliabilityRate - guardsRatingDecrease);
            }
            if(reportReliabilityRate == maxNumberOfReliabilityStars && verification == Verification.Deny){
                if(currentGuardReliabilityRate + guardRatingIncrease >= maxNumberOfReliabilityStars)
                    guard.setReliabilityRate(maxNumberOfReliabilityStars);
                else
                    guard.setReliabilityRate(currentGuardReliabilityRate + guardRatingIncrease);
            }
        }
    }
    public void calculateReporterReliabilityRate() {
        User reporter = report.getReporter();
        float currentReporterReliabilityRate = reporter.getReliabilityRate();
        if(reportReliabilityRate <= 1){
            if(currentReporterReliabilityRate - reporterRatingDecrease <=minNumberOfReliabilityStars)
                reporter.setReliabilityRate(minNumberOfReliabilityStars);
            else
                reporter.setReliabilityRate(currentReporterReliabilityRate - reporterRatingDecrease);
        }
        if(reportReliabilityRate == maxNumberOfReliabilityStars){
            if(currentReporterReliabilityRate + reporterRatingIncrease >= maxNumberOfReliabilityStars)
                reporter.setReliabilityRate(maxNumberOfReliabilityStars);
            else
                reporter.setReliabilityRate(currentReporterReliabilityRate + reporterRatingIncrease);
        }
    }
}
