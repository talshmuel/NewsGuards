package newsGuardServer;

import data.transfer.object.LoginDTO;
import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.NewReportDTO;
import data.transfer.object.user.NewUserDTO;
import logic.engine.Engine;
import logic.engine.report.Report;
import logic.engine.report.ReportsManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.sql.SQLException;
import java.util.Date;


@ComponentScan(basePackages = {"logic.engine", "newsGuardServer"})
@SpringBootApplication()
public class Server {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(Server.class, args);
        //checkLoginDetails();
        //checkComments();
        //checkLikes();
        //check();
        ReportsManager manager = new ReportsManager();
        Report report = manager.findAndRestoreReportFromDB(1);

        System.out.print(report.getComments().size());
        System.out.print(report.getCountUsersWhoLiked());
    }

    public static void check()
    {
        Date date = new Date();
        Engine engine = new Engine();
        NewUserDTO newUser = new NewUserDTO("tal", "shmuel","israel","tooliii@gmail.com","123456","http//:www.com","0525440635",true);
        engine.createNewUser(newUser);
        NewReportDTO newReport = new NewReportDTO("hello world,this is tal","http://", 1, true,date,1,1);
        engine.addNewReportAndStartVerificationProcess(newReport);
        NewReportDTO newReport1 = new NewReportDTO("hello world,this is tal again","http://", 1, true,date,1,1);
        engine.addNewReportAndStartVerificationProcess(newReport1);
        CommentDTO newComment1 = new CommentDTO(1,"this is comment1",1,true);
        engine.addCommentToReport(newComment1);
        CommentDTO newComment2 = new CommentDTO(1,"this is comment2",1,true);
        engine.addCommentToReport(newComment2);
        CommentDTO newComment3 = new CommentDTO(2,"this is comment1",1,true);
        engine.addCommentToReport(newComment3);
        CommentDTO newComment4 = new CommentDTO(2,"this is comment2",1,true);
        engine.addCommentToReport(newComment4);
        engine.addOrRemoveLikeToReport(1,1);
        engine.addOrRemoveLikeToReport(2,1);
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
        NewUserDTO newUser = new NewUserDTO("nitzan hamalca", "sdeor","israel","nitaikoren@gmail.com","123456","http//:www.com","0525440635",true);
        engine.createNewUser(newUser);
        NewReportDTO newReport = new NewReportDTO("hello everyone ,this is nitzan","http://", 1, true,date,1,1);
        engine.addNewReportAndStartVerificationProcess(newReport);
        CommentDTO newComment = new CommentDTO(1,"this is comment",1,true);
        engine.addCommentToReport(newComment);
    }

    public static void checkLoginDetails()
    {
        Engine engine = new Engine();
        LoginDTO login = new LoginDTO("john.asdf@gmail.com","12345");
        int id = engine.checkLoginDetails(login);
        System.out.println("the id is:" + id);
    }




}
