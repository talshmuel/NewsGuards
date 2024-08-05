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
    }
}
