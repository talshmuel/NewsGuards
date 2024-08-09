package newsGuardServer;

import data.transfer.object.report.CommentDTO;
import data.transfer.object.report.NewReportDTO;
import data.transfer.object.user.NewUserDTO;
import data.transfer.object.user.UserDTO;
import logic.engine.Engine;
import logic.engine.report.Comment;
import logic.engine.report.Report;
import logic.engine.report.ReportsManager;
import logic.engine.user.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;


@ComponentScan(basePackages = {"logic.engine"})
@SpringBootApplication()
public class Server {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(Server.class, args);
        checkComments();
        //checkLikes();
        //check();
    }

//    public static void check()
//    {
//        Date date = new Date();
//        Engine engine = new Engine();
//        NewUserDTO newUser = new NewUserDTO("tal", "shmuel","israel","tal@gmail.com","123456","http//:www.com","0525440635",true);
//        engine.createNewUser(newUser);
//        NewReportDTO newReport = new NewReportDTO("hello world,this is nitzan","http://", 1, true,date,1,1);
//        engine.addNewReportAndStartVerificationProcess(newReport);
//        NewReportDTO newReport1 = new NewReportDTO("hello world,this is tal","http://", 1, true,date,1,1);
//        engine.addNewReportAndStartVerificationProcess(newReport1);
//
//    }

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

}
