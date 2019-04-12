package ch.fhnw.wodss.webapplication.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.fhnw.wodss.webapplication.components.employee.EmployeeRepository;
import ch.fhnw.wodss.webapplication.components.project.ProjectDto;
import ch.fhnw.wodss.webapplication.components.project.ProjectRepository;
import ch.fhnw.wodss.webapplication.components.project.ProjectService;
import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceUnitTest {

    @Mock
    private ProjectRepository projectRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private AuthenticatedEmployee mockEmployee;

    @Mock
    private ProjectDto mockProject;

    private ProjectService projectService;

    @Before
    public void setUp() throws Exception {
        projectService = new ProjectService(projectRepo, employeeRepo);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenProjectIsNull_thenCannotCreateProject() {
        projectService.createProject(null, mockEmployee);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenEmployeeIsNull_thenCannotCreateProject() {
        projectService.createProject(mockProject, null);
    }

    @Test(expected = IllegalStateException.class)
    public void whenNotAuthorized_thenCannotCreateProject() {
        ProjectDto project = new ProjectDto();
        project.setProjectManagerId(1l);
        mockEmployee.setId(2l);
        projectService.createProject(project, mockEmployee);
    }  







}