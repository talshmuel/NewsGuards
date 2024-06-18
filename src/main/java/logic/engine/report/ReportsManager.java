package logic.engine.report;

import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.NewReportDTO;
import logic.engine.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportsManager {
    private Map<Integer, Report> reports;

    public ReportsManager() {
        this.reports = new HashMap<>();
    }

    public Report addNewReport(NewReportDTO newReportDTO, User reporter){
        ArrayList<Genre> genreEnumList = convertGenreStrToEnum(newReportDTO.getGenres());
        Report newReport = new Report(newReportDTO.getText(), newReportDTO.getImageURL(),
                reporter, newReportDTO.isAnonymousReport(), newReportDTO.getIDOfUsersTags()
                ,genreEnumList, newReportDTO.getLocation(), newReportDTO.getTimeReported());
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
        reports.get(reportID).addOrRemoveLike(userID);
    }
    public void addNewComment(Comment comment){
        reports.get(comment.getReportID()).addNewComment(comment);
    }

}
