package logic.engine.reliability.management;

import logic.engine.Engine;
import logic.engine.report.Report;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReportVerificationProcess {
    Engine engine;
    int timeWindowToVerify = 2; //todo change, it is just a try
    TimeUnit units = TimeUnit.MINUTES;//todo change, it is just a try
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private boolean isRunning = true;
    private Integer reportID;
    private Set<Integer> guardsThatNeedToVerify;

    private Rate reliabilityRate;
    public ReportVerificationProcess(int reportIDToVerify, List<Integer> guards, Engine engine){
        this.reportID = reportIDToVerify;
        this.engine = engine;
        guardsThatNeedToVerify = new HashSet<>();
        for(Integer guardID : guards){
            guardsThatNeedToVerify.add(guardID);
        }
        scheduler.schedule(this::stop24HourWindowToVerify, timeWindowToVerify, units);
    }
    public void removeGuardThatVerified(int guardID){
        guardsThatNeedToVerify.remove(guardID);
    }

    public Set<Integer> getGuardsThatNeedToVerify() {
        return guardsThatNeedToVerify;
    }
    private void stop24HourWindowToVerify() {
        isRunning = false;
        scheduler.shutdown();
        engine.stopWindowToVerify(reportID, guardsThatNeedToVerify);
    }
    public boolean isRunning() {
        return isRunning;
    }

}
