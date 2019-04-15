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
     * Heroku uses a single URL to identify a database connection including the username and password - sadly this
     * URL is not really JDBC compliant (or many JDBC connection pools can't handle it), so we have to rebuild the URL
     * manually
     * <p>
     * Required proper JDBC URL for Jooq:
     * <p>
     * DATABASE_URL=jdbc:postgresql://REPLACEHOSTNAME:5432/REPLACEDATABASE
     * DATABASE_USERNAME=REPLACEUSER
     * DATABASE_PASSWORD=REPLACEPASSWORD
     * <p>
     * Given URL by Heroku (We have to parse it to provide a proper JDBC connection):
     * <p>
     * DATABASE_URL=postgres://REPLACEUSER:REPLACEPASSWORD@REPLACEHOSTNAME:5432/REPLACEDATABASE
     *
     * @return Proper data source based on the Heroku URL input for Jooq
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
