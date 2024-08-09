package newsGuardServer;

import data.transfer.object.report.NewReportDTO;
import data.transfer.object.user.NewUserDTO;
import data.transfer.object.user.UserDTO;
import logic.engine.Engine;
import logic.engine.report.Report;
import logic.engine.report.ReportsManager;
import logic.engine.user.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.sql.SQLException;
import java.util.ArrayList;


@ComponentScan(basePackages = {"logic.engine"})
@SpringBootApplication
public class Server {

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(Server.class, args);
        //        checkLikes();
  //      check();
    }

//    public static void check()
//    {
//        Engine engine = new Engine();
//        NewUserDTO newUser = new NewUserDTO("nitai", "koren","israel","nitaikoren@gmail.com","123456","http//:www.com","0525440635",true);
//        engine.createNewUser(newUser);
//        NewReportDTO newReport = new NewReportDTO("hello world","http://", 1, true);
//        engine.addNewReportAndStartVerificationProcess(newReport);
//        NewReportDTO newReport1 = new NewReportDTO("nice world","http://", 1, true);
//        engine.addNewReportAndStartVerificationProcess(newReport1);
//
//    }

//     public static void checkLikes()
//     {
//        NewUserDTO newUser = new NewUserDTO("nitzan", "sdeor","israel","nitaikoren@gmail.com","123456","http//:www.com","0525440635",true);
//
//         User user = new User(newUser);
//         Report report = new Report("hello world","http://", user, true);
//         report.addOrRemoveLike(user.getID());
//     }

}
