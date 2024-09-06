package logic.engine.reliability.management;

import logic.engine.report.Report;
import logic.engine.user.User;
import newsGuardServer.DatabaseConfig;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReportVerificationProcess {
    int timeWindowToVerify = 5; //todo change, it is just a try
    TimeUnit units = TimeUnit.MINUTES;//todo change, it is just a try
    float maxNumberOfReliabilityStars = 5;
    float minNumberOfReliabilityStars = 0;
    float guardsRatingDecrease = 0.2f;
    float guardRatingIncrease = 0.1f;
    float reporterRatingDecrease = 1f;
    float reporterRatingIncrease = 0.1f;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    //private boolean isRunning = true;
    private Report report;
    private Map<Integer, GuardVerification> guardsVerification;
    private float reportReliabilityRate;
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;

    public ReportVerificationProcess(Report report, Map<User, Verification> guards){
        this.report = report;
        guardsVerification = new HashMap<>();
        guards.forEach((guard, verification)->{
            guardsVerification.put(guard.getID(), new GuardVerification(guard, verification));
            //guard.addReportToVerify(report);
        });
//        for(User guard : guards){
//            guardsVerification.put(guard.getID(), new GuardVerification(guard, Verification.Pending));
//            guard.addReportToVerify(report);
//        }
        scheduler.schedule(this::stopWindowToVerify, timeWindowToVerify, units);
    }
    public void updateGuardVerification(int guardID, Verification verification,User guard){
        report.updateGuardVerification(guardID, verification);
        GuardVerification guardVerification = guardsVerification.get(guardID);
        guardVerification.setVerification(verification);
        guard.updateGuardVerification(report.getID(), verification);
    }
    private void stopWindowToVerify() {
        scheduler.shutdown();

        for(GuardVerification guardVerification : guardsVerification.values()){
            guardVerification.getGuard().removeReportToVerify(report.getID());
        }
        calculateReliabilityRate();
        deleteReportFromVerificationProcessInDB();
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
        report.setReliabilityRateInDB(reportReliabilityRate);
    }
    public void calculateGuardsReliabilityRate(){
        for(GuardVerification guardsVerification : guardsVerification.values()){
            Verification verification = guardsVerification.getVerification();
            User guard = guardsVerification.getGuard();
            float currentGuardReliabilityRate = guard.getReliabilityRate();

            if(reportReliabilityRate <= 1 && verification == Verification.Approve){
                if(currentGuardReliabilityRate - guardsRatingDecrease <=minNumberOfReliabilityStars) {
                    guard.setReliabilityRate(minNumberOfReliabilityStars);
                    guard.setReliabilityRateInDB(minNumberOfReliabilityStars);
                }
                else {
                    guard.setReliabilityRate(currentGuardReliabilityRate - guardsRatingDecrease);
                    guard.setReliabilityRateInDB(currentGuardReliabilityRate - guardsRatingDecrease);
                }
            }
            if(reportReliabilityRate >= 4 && verification == Verification.Deny){
                if(currentGuardReliabilityRate + guardRatingIncrease >= maxNumberOfReliabilityStars) {
                    guard.setReliabilityRate(maxNumberOfReliabilityStars);
                    guard.setReliabilityRateInDB(maxNumberOfReliabilityStars);
                }
                else {
                    guard.setReliabilityRate(currentGuardReliabilityRate + guardRatingIncrease);
                    guard.setReliabilityRateInDB(currentGuardReliabilityRate + guardRatingIncrease);
                }
            }
        }
    }
    public void calculateReporterReliabilityRate() {
        User reporter = report.getReporter();
        float currentReporterReliabilityRate = reporter.getReliabilityRate();
        if(reportReliabilityRate <= 1){
            if(currentReporterReliabilityRate - reporterRatingDecrease <=minNumberOfReliabilityStars) {
                reporter.setReliabilityRate(minNumberOfReliabilityStars);
                reporter.setReliabilityRateInDB(minNumberOfReliabilityStars);
            }
            else {
                reporter.setReliabilityRate(currentReporterReliabilityRate - reporterRatingDecrease);
                reporter.setReliabilityRateInDB(currentReporterReliabilityRate - reporterRatingDecrease);
            }
        }
        if(reportReliabilityRate == maxNumberOfReliabilityStars){
            if(currentReporterReliabilityRate + reporterRatingIncrease >= maxNumberOfReliabilityStars) {
                reporter.setReliabilityRate(maxNumberOfReliabilityStars);
                reporter.setReliabilityRateInDB(maxNumberOfReliabilityStars);
            }
            else {
                reporter.setReliabilityRate(currentReporterReliabilityRate + reporterRatingIncrease);
                reporter.setReliabilityRateInDB(currentReporterReliabilityRate + reporterRatingIncrease);
            }
        }
    }

    public void deleteReportFromVerificationProcessInDB()
    {
        String sql = "DELETE FROM reports_verification_process WHERE report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, report.getID());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }

        sql = "DELETE FROM guards_verification WHERE report_id = ?";

        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, report.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exception
        }
    }
}
