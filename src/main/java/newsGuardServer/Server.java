package newsGuardServer;

import data.transfer.object.LoginDTO;
import data.transfer.object.location.LocationDTO;
import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.NewReportDTO;
import data.transfer.object.report.ReportDTO;
import data.transfer.object.user.NewUserDTO;
import logic.engine.Engine;
import logic.engine.reliability.management.Verification;
import logic.engine.report.Report;
import logic.engine.report.ReportsManager;
import logic.engine.user.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;


@ComponentScan(basePackages = {"logic.engine", "newsGuardServer"})
@SpringBootApplication()
public class Server {
    private static final DatabaseConfig DB_CONFIG = DatabaseConfig.POSTGRESQL;

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(Server.class, args);
//        Verification ver = Verification.Approve;
//        int user = 2;
//        updateGuardVerificationInDB(user,ver);
//        Engine engine = new Engine();
//        LocationDTO location = new LocationDTO(12,1,3);
//        engine.saveUserLocation(location);
        //checkLoginDetails();
        //checkComments();
        //checkLikes();

 //       check();
//        ReportsManager manager = new ReportsManager();
//        Report report = manager.findAndRestoreReportFromDB(1);
//        System.out.print(report.getComments().size());
//        System.out.print(report.getCountUsersWhoLiked());
    }


//    public static void updateGuardVerificationInDB(int guardID, Verification verification) {
//        int verificationInt = verification.toInt();
//        String sql = "UPDATE guards_verification SET user_response = ?  WHERE report_id = ? AND user_id = ?";
//
//        try (Connection connection = DriverManager.getConnection(DB_CONFIG.getUrl(), DB_CONFIG.getUsername(), DB_CONFIG.getPassword());
//             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//
//            preparedStatement.setInt(1, verificationInt);
//            preparedStatement.setInt(2, 32);
//            preparedStatement.setInt(3, guardID);
//            preparedStatement.executeUpdate();
//
//        } catch (SQLException e) {
//            e.printStackTrace(); // Handle SQL exception
//        }
//    }

    public static void check()
    {
        Date date = new Date();
        Engine engine = new Engine();
//        ArrayList<ReportDTO> reports = engine.getLastTwentyReportsToHomePage();
//        for (ReportDTO report : reports) {
//            System.out.print(report.getText());
//            System.out.print("\n");
//        }
        NewUserDTO newUser = new NewUserDTO("dor", "dani","israel","dan@gmail.com","123456","http//:www.com","0525440635",true);
        //engine.createNewUser(newUser);
        User user = new User(newUser,3,false);
        user.restoreReportsThatNeedToVerify();
//        NewReportDTO newReport = new NewReportDTO("wondeull world","http://", 1, true,date,1,1);
//        engine.addNewReportAndStartVerificationProcess(newReport);
 //       NewReportDTO newReport1 = new NewReportDTO("try store the guards","http://", 1, true,date,32.0468679,34.7630199);
 //       engine.addNewReportAndStartVerificationProcess(newReport1);
//        CommentDTO newComment1 = new CommentDTO(1,"this is comment1",1,true);
//        engine.addCommentToReport(newComment1);
//        CommentDTO newComment2 = new CommentDTO(1,"this is comment2",1,true);
//        engine.addCommentToReport(newComment2);
//        CommentDTO newComment3 = new CommentDTO(2,"this is comment1",1,true);
//        engine.addCommentToReport(newComment3);
//        CommentDTO newComment4 = new CommentDTO(2,"this is comment2",1,true);
//        engine.addCommentToReport(newComment4);
//        engine.addOrRemoveLikeToReport(1,1);
//        engine.addOrRemoveLikeToReport(2,1);
    }

//     public static void checkLikes()
//     {
//         Engine engine = new Engine();
//         Date date = new Date();
//         NewUserDTO newUser = new NewUserDTO("nitzan", "sdeor","israel","nitaikoren@gmail.com","123456","http//:www.com","0525440635",true);
//         engine.createNewUser(newUser);
//         NewReportDTO newReport = new NewReportDTO("hello world,this is nitzan","http://", 1, true,date,1,1);
//         engine.addNewReportAndStartVerificationProcess(newReport);
//         engine.addOrRemoveLikeToReport(1,1);
//
//     }
    public static void checkComments()
    {
        Engine engine = new Engine();
        Date date = new Date();
//        NewUserDTO newUser = new NewUserDTO("nitzan hamalca", "sdeor","israel","nitaikoren@gmail.com","123456","http//:www.com","0525440635",true);
//        engine.createNewUser(newUser);
//        NewReportDTO newReport = new NewReportDTO("hello everyone ,this is nitzan","http://", 1, true,date,1,1);
//        engine.addNewReportAndStartVerificationProcess(newReport);
        CommentDTO newComment = new CommentDTO(5,"this is comment",1,true, "Tal added this, niztan check if need to change");
        engine.addCommentToReport(newComment);
    }

    public static void checkLoginDetails()
    {
        Engine engine = new Engine();
        LoginDTO login = new LoginDTO("tal@gmail.com","123456");
        int id = engine.checkLoginDetails(login);
        System.out.println("the id is:" + id);
    }




}
