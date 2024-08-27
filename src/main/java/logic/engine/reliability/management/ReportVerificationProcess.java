package logic.engine.reliability.management;

import logic.engine.report.Report;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReportVerificationProcess {
    private Report report;
    private Set<Integer> guardsThatNeedToVerify;
    private Rate reliabilityRate;
    public ReportVerificationProcess(Report reportToVerify, List<Integer> guards){
        this.report = reportToVerify;
        guardsThatNeedToVerify = new HashSet<>();
        for(Integer guardID : guards){
            guardsThatNeedToVerify.add(guardID);
        }
    }

    public void removeGuardThatVerified(int guardID){
        guardsThatNeedToVerify.remove(guardID);
    }

    public Set<Integer> getGuardsThatNeedToVerify() {
        return guardsThatNeedToVerify;
    }
}
