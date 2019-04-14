package ch.fhnw.wodss.webapplication.service;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeRepository;
import ch.fhnw.wodss.webapplication.components.project.ProjectDto;
import ch.fhnw.wodss.webapplication.components.project.ProjectRepository;
import ch.fhnw.wodss.webapplication.components.project.ProjectService;
import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import static org.mockito.BDDMockito.given;

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
        given(mockProject.getProjectManagerId()).willReturn(1l);
        given(mockEmployee.getId()).willReturn(2l);
        projectService.createProject(mockProject, mockEmployee);
    }

    @Test(expected = InternalException.class)
    public void whenProjectCannotBeMapped_thenThrowException() {
        given(mockProject.getProjectManagerId()).willReturn(1l);
        given(mockEmployee.getId()).willReturn(1l);
        given(projectRepo.saveProject(mockProject)).willReturn(Optional.empty());
        projectService.createProject(mockProject, mockEmployee);
    }

    @Test(expected = EntityNotFoundException.class)
    public void whenEmployeeNotFound_thenThrowException() {
        given(mockProject.getProjectManagerId()).willReturn(1l);
        given(mockEmployee.getId()).willReturn(1l);
        given(projectRepo.saveProject(mockProject))
        .willReturn(Optional.of(mockProject));
        given(employeeRepo.getEmployeeById(mockProject.getProjectManagerId()))
        .willReturn(Optional.empty());
        projectService.createProject(mockProject, mockEmployee);
    }









}