package logic.engine.reliability.management;

import logic.engine.report.Report;
import logic.engine.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportVerificationProcess {
    static int IDGenerator = 0;
    int ID;
    Report report;
    Map<User, GuardResponse> guardsResponses;
    Rate reliabilityRate;
    public ReportVerificationProcess(Report reportToVerify, ArrayList<User> guards){
        ID = ++IDGenerator;
        this.report = reportToVerify;
        guardsResponses = new HashMap<>();
        guards.forEach((guard)->{
            guardsResponses.put(guard, GuardResponse.Pending);
        });
    }


    public void sendVerificationRequestToGuards(){
        //todo send to each guard a verification request
    }
    public void setGuardResponse(User guard, GuardResponse guardResponse){
        guardsResponses.put(guard, guardResponse);
    }
}
