package ch.fhnw.wodss.webapplication.components;

import ch.fhnw.wodss.webapplication.components.project.ProjectDto;
import ch.fhnw.wodss.webapplication.components.token.Token;
import ch.fhnw.wodss.webapplication.configuration.DatabaseConfiguration;
import net.minidev.json.JSONObject;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = IntegrationTests.Initializer.class)
@Testcontainers
public class IntegrationTests {

    @Autowired
    private TestRestTemplate template;

    @Container
    private static final PostgreSQLContainer DB_CONTAINER = new PostgreSQLContainer("postgres:11.2").withDatabaseName("dev_wodss_db").withUsername("user").withPassword("password");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("spring.datasource.url=" + DB_CONTAINER.getJdbcUrl(), "spring.datasource.username=" + DB_CONTAINER.getUsername(), "spring.datasource.password=" + DB_CONTAINER.getPassword()).applyTo(configurableApplicationContext.getEnvironment());
        }
    }


    @BeforeAll
    public static void setupMigration() {
        Flyway flyway = Flyway.configure().dataSource(DB_CONTAINER.getJdbcUrl(), DB_CONTAINER.getUsername(), DB_CONTAINER.getPassword()).load();
        flyway.migrate();
    }

    @Test
    public void whenAdministrator_thenReturnRequestedProject() {
        JSONObject request = new JSONObject();
        request.put("emailAddress", "simon.waechter@students.fhnw.ch");
        request.put("rawPassword", "123456aA");
        ResponseEntity<Token> token = template.postForEntity("/api/token", request, Token.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(Objects.requireNonNull(token.getBody()).getToken());
        HttpEntity entity = new HttpEntity<>(null, headers);
        ResponseEntity<ProjectDto> project = template.exchange("/api/project/{id}", HttpMethod.GET, entity, ProjectDto.class, "e3883e80-e1e6-4ebd-96fb-8ebb51fd9d17");
        assertEquals(HttpStatus.OK, project.getStatusCode());
    }
}
