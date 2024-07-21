import org.springframework.boot.jdbc.DataSourceBuilder;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder
                .create()
                .url("jdbc:postgresql://localhost:5432/your_database_name")
                .username("your_database_username")
                .password("your_database_password")
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
