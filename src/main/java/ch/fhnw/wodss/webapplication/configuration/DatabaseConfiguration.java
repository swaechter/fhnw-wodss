package ch.fhnw.wodss.webapplication.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    private final String jdbcUrl;

    public DatabaseConfiguration(@Value("${spring.datasource.url}") String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    /**
     * Heroku uses a single URL to identify a database connection - sadly this URL is not really JDBC compliant, so we
     * have to reparse the URL manually
     *
     * @return Proper data source based on the Heroku URL input
     */
    @Bean
    public DataSource getDataSource() {
        // Split the original line
        String[] arguments = jdbcUrl.split(":|\\@|\\//|\\/");
        if (arguments.length != 7) {
            throw new IllegalArgumentException("Invalid datasource URL");
        }

        // Reuse the username and password - they are fine
        String username = arguments[2];
        String password = arguments[3];

        // Replace their strange jdbc:postgres with jdbc:postgesql + reuse the original values
        String url = "jdbc:postgresql://" + arguments[4] + ":" + arguments[5] + "/" + arguments[6];

        // Create the origjnal Hikari config including username and password (Hikari can't parse the credentials in the original URL)
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName("org.postgresql.Driver");
        return new HikariDataSource(hikariConfig);
    }
}
