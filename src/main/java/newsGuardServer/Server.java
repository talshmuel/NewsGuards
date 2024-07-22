package newsGuardServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@ComponentScan(basePackages = {"logic.engine", "newsGuardServer"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//@SpringBootApplication כשנחזיר את הדאטה בייס צריך להחזיר לזה ולמחוק את מה שמעל
public class Server {
    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }
}
