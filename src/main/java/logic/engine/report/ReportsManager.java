package logic.engine.report;

import data.transfer.object.report.NewReportDTO;
import logic.engine.user.User;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ReportsManager {
    private Map<Integer, Report> reports;

    public ReportsManager() {
        this.reports = new HashMap<>();
    }

    public Report addNewReport(NewReportDTO newReportDTO, User reporter){
        Report newReport = new Report(newReportDTO.getText(), newReportDTO.getImageURL(),
                reporter, newReportDTO.isAnonymousReport()
                , new Point2D.Double(newReportDTO.getLatitude(),
                newReportDTO.getLongitude()), newReportDTO.getDateTime());
        reports.put(newReport.getID(), newReport);

        return newReport;
    }


    public void addOrRemoveLike(int reportID, int userID){
        Report report = reports.get(reportID);
        if(report == null)
            throw new NoSuchElementException("Error - there is no report in the system whose ID number is: " + reportID);
        report.addOrRemoveLike(userID);
    }
    public void addNewComment(Comment comment){
        Report report = reports.get(comment.getReportID());
        if(report == null){
            throw new NoSuchElementException("Error - there is no report in the system whose ID number is:" + comment.getReportID());
        }
        report.addNewComment(comment);
    }


}
