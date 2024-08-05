package logic.engine.report;

import data.transfer.object.report.NewReportDTO;
import data.transfer.object.report.ReportDTO;
import logic.engine.user.User;
import logic.engine.user.registration.UserReportsPreferences;

import java.awt.geom.Point2D;
import java.io.IOException;
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
        ArrayList<Genre> genreEnumList = convertGenreStrToEnum(newReportDTO.getGenres());
        Report newReport = new Report(newReportDTO.getText(), newReportDTO.getImageURL(),
                reporter, newReportDTO.isAnonymousReport()
                ,genreEnumList, new Point2D.Double(newReportDTO.getLatitude(),
                newReportDTO.getLongitude()), newReportDTO.getDateTime());
        reports.put(newReport.getID(), newReport);

        return newReport;
    }

    private ArrayList<Genre> convertGenreStrToEnum(ArrayList<String> genreStrList){
        ArrayList<Genre> genreEnumList = new ArrayList<>();
        genreStrList.forEach((genreStr)->{
            genreEnumList.add(Genre.convertStringToGenre(genreStr));
        });
        return genreEnumList;
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

    public ArrayList<ReportDTO> getReportsFilteredByPreferences(UserReportsPreferences preferences) throws Exception {
        ArrayList<ReportDTO> reportsFiltered = new ArrayList<>();
        for(Report report : reports.values()){
            if(report.hasAtLeastOneOfGenresInlist(preferences.getGenrePreference()) &&
                    preferences.getCountriesPreference().contains(report.getCountry())&&
                    report.getReliabilityRate() >= preferences.getReliabilityRatePreference()){
                reportsFiltered.add(report.getReportDTO());
            }
        }
        return reportsFiltered;
    }

}
