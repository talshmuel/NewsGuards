package newsGuardServer;

import data.transfer.object.user.NewUserDTO;
import logic.engine.Engine;
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
        check();
    }

    public static void check() throws SQLException {
        ArrayList<String> genre = new ArrayList<>();
        genre.add("1");
        ArrayList<Integer> reliability = new ArrayList<>();
        reliability.add(1);
        ArrayList<String> countries = new ArrayList<>();
        countries.add("Holand");
        NewUserDTO userDTO = new NewUserDTO("tal", "shmuel", "israel", "nitzan@gmail.com",
                "1234", "http:wdqwd", "0525440634",
                genre, reliability, countries,
                true);
        Engine engine = new Engine();
        engine.createNewUser(userDTO);
    }

}
