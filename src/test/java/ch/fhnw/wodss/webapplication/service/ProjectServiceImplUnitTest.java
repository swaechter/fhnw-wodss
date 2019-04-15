package ch.fhnw.wodss.webapplication.service;


import ch.fhnw.wodss.webapplication.components.employee.EmployeeRepository;
import ch.fhnw.wodss.webapplication.components.project.ProjectDto;
import ch.fhnw.wodss.webapplication.components.project.ProjectRepository;
import ch.fhnw.wodss.webapplication.components.project.ProjectServiceImpl;
import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplUnitTest {
    @Mock
    private ProjectRepository projectRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private AuthenticatedEmployee mockEmployee;

    @Mock
    private ProjectDto mockProject;

    private ProjectServiceImpl projectService;

    @BeforeEach
    public void setUp()
    {
        projectService = new ProjectServiceImpl(projectRepo, employeeRepo);
    }

    @Test
    public void whenProjectOrEmployeeIsNull_thenCannotCreateProject()
    {
        assertException(IllegalArgumentException.class, null, mockEmployee);
        assertException(IllegalArgumentException.class, mockProject, null);

    }

    @Test
    public void whenNotAuthorized_thenCannotCreateProject()
    {
        setupProjectWithManager(1L, 2L);
        assertException(IllegalStateException.class, mockProject, mockEmployee);
    }

    @Nested
    public class whenEmployeeIsProjectManager {
        @BeforeEach
        public void setUp()
        {
            setupProjectWithManager(1L, 1L);
        }

        @Test
        public void whenProjectCannotBeSavedToDatabase_thenThrowException()
        {
            given(projectRepo.saveProject(mockProject)).willReturn(Optional.empty());
            assertException(InternalException.class, mockProject, mockEmployee);
        }

        @Test
        public void whenEmployeeNotFound_thenThrowException()
        {
            given(projectRepo.saveProject(mockProject)).willReturn(Optional.of(
                mockProject));
            given(employeeRepo.getEmployeeById(mockProject.getProjectManagerId())).willReturn(
                Optional.empty());
            assertException(EntityNotFoundException.class,
                            mockProject,
                            mockEmployee);
        }
    }

    private void setupProjectWithManager(
        long projectManagerId,
        long employeeId)
    {
        given(mockProject.getProjectManagerId()).willReturn(projectManagerId);
        given(mockEmployee.getId()).willReturn(employeeId);
    }

    private void assertException(
        Class<? extends Throwable> cl,
        ProjectDto project,
        AuthenticatedEmployee employee)
    {
        assertThrows(cl, () -> projectService.createProject(project, employee));
    }
}
