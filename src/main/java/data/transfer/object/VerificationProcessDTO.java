package data.transfer.object;

import data.transfer.object.report.ReportDTO;

import java.util.Map;

public class VerificationProcessDTO {
    int ID;
    ReportDTO reportDTO;
    Map<Integer, Integer> responsesMapByUserID;
}
