package logic.engine.reliability.management;

import logic.engine.report.Report;
import logic.engine.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportVerificationProcess {
    static int IDGenerator = 0;
    private int ID;
    private Report report;
    private Map<Integer, GuardResponse> guardsResponses;
    private Rate reliabilityRate;
    public ReportVerificationProcess(Report reportToVerify, List<Integer> guards){
        ID = ++IDGenerator;
        this.report = reportToVerify;
        guardsResponses = new HashMap<>();
        guards.forEach((guardID)->{
            guardsResponses.put(guardID, GuardResponse.Pending);
        });
    }

    public int getID() {
        return ID;
    }

    public void sendVerificationRequestToGuards(){
        //todo send to each guard a verification request
    }
    public void setGuardResponse(Integer guard, GuardResponse guardResponse){
        guardsResponses.put(guard, guardResponse);
    }
}
