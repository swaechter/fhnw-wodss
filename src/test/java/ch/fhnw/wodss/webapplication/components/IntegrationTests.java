package ch.fhnw.wodss.webapplication.components;

import ch.fhnw.wodss.webapplication.components.project.ProjectDto;
import ch.fhnw.wodss.webapplication.components.token.Token;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    private JSONObject request = new JSONObject();
    private Token token;
    private HttpHeaders headers = new HttpHeaders();
    private static ProjectDto expected = new ProjectDto();

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("spring.datasource.url=" + DB_CONTAINER.getJdbcUrl(), "spring.datasource.username=" + DB_CONTAINER.getUsername(), "spring.datasource.password=" + DB_CONTAINER.getPassword()).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @BeforeAll
    public static void setupExpectedProject() {
        expected.setId(UUID.fromString("e3883e80-e1e6-4ebd-96fb-8ebb51fd9d17"));
        expected.setProjectManagerId(UUID.fromString("8d7a6f19-676d-42de-8f3a-039588a49c5f"));
        expected.setStartDate(LocalDate.of(2019, 2, 1));
        expected.setEndDate(LocalDate.of(2019, 8, 16));
        expected.setName("IP6 Philipp Lüthi & Thibault Gagnaux");
        expected.setFtePercentage(2L);
    }

    @Nested
    public class whenDeveloper {
        @BeforeEach
        public void setupDeveloper() {
            request.put("emailAddress", "thibault.gagnaux@students.fhnw.ch");
            request.put("rawPassword", "123456aA");
            token = Objects.requireNonNull(template.postForEntity("/api/token", request, Token.class).getBody());
            headers.setBearerAuth(token.getToken());
        }

        @Test
        public void whenDeveloper_thenReturnProjectIfAssigned() {
            HttpEntity entity = new HttpEntity<>(null, headers);
            ResponseEntity<ProjectDto> response = template.exchange("/api/project/{id}", HttpMethod.GET, entity, ProjectDto.class, expected.getId());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            ProjectDto project = Objects.requireNonNull(response.getBody());
            assertProjectDto(expected, project);
        }

        @Test
        public void whenDeveloper_thenReturnAllAssignedProjects() {
            HttpEntity entity = new HttpEntity<>(null, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/api/project/").queryParam("projectManagerId", "").queryParam("fromDate", "2019-01-01").queryParam("toDate", "2019-12-01");
            ResponseEntity<List<ProjectDto>> response = template.exchange(builder.toUriString(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<ProjectDto>>() {
            });
            assertEquals(HttpStatus.OK, response.getStatusCode());
            List<ProjectDto> projects = Objects.requireNonNull(response.getBody());
            assertEquals(2, projects.size());
            ProjectDto project = projects.get(0);
            assertProjectDto(expected, project);
        }
    }

    private void assertProjectDto(ProjectDto expected, ProjectDto actual) {
        assertEquals(actual.getStartDate(), expected.getStartDate());
        assertEquals(actual.getEndDate(), expected.getEndDate());
        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getFtePercentage(), expected.getFtePercentage(), 0.0001);
        assertEquals(actual.getId(), expected.getId());
        assertEquals(actual.getProjectManagerId(), expected.getProjectManagerId());
    }

    @Nested
    public class whenAdministrator {
        @BeforeEach
        public void setupAdministrator() {
            request.put("emailAddress", "simon.waechter@students.fhnw.ch");
            request.put("rawPassword", "123456aA");
        }

        @Test
        public void thenReturnAllProjects() {
            ResponseEntity<Token> token = template.postForEntity("/api/token", request, Token.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(Objects.requireNonNull(token.getBody()).getToken());
            HttpEntity entity = new HttpEntity<>(null, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("/api/project/").queryParam("projectManagerId", "").queryParam("fromDate", "2019-01-01").queryParam("toDate", "2019-12-01");
            ResponseEntity<List<ProjectDto>> response = template.exchange(builder.toUriString(), HttpMethod.GET, entity, new ParameterizedTypeReference<List<ProjectDto>>() {
            });
            assertEquals(HttpStatus.OK, response.getStatusCode());
            List<ProjectDto> projects = Objects.requireNonNull(response.getBody());
            assertEquals(3, projects.size());
        }

        @Test
        public void thenReturnRequestedProject() {
            ResponseEntity<Token> token = template.postForEntity("/api/token", request, Token.class);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(Objects.requireNonNull(token.getBody()).getToken());
            HttpEntity entity = new HttpEntity<>(null, headers);
            ResponseEntity<ProjectDto> response = template.exchange("/api/project/{id}", HttpMethod.GET, entity, ProjectDto.class, expected.getId());
            assertEquals(HttpStatus.OK, response.getStatusCode());
            ProjectDto project = Objects.requireNonNull(response.getBody());
            assertProjectDto(expected, project);
        }
    }
}
