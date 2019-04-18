package ch.fhnw.wodss.webapplication.service;


import ch.fhnw.wodss.webapplication.components.employee.EmployeeRepository;
import ch.fhnw.wodss.webapplication.components.employee.Role;
import ch.fhnw.wodss.webapplication.components.project.ProjectDto;
import ch.fhnw.wodss.webapplication.components.project.ProjectRepository;
import ch.fhnw.wodss.webapplication.components.project.ProjectService;
import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import ch.fhnw.wodss.webapplication.exceptions.InvalidActionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
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

    @BeforeEach
    public void setUp() {
        projectService = new ProjectService(projectRepo, employeeRepo);
    }

    @Test
    public void whenProjectOrEmployeeIsNull_thenCannotCreateProject() {
        assertThrows(InvalidActionException.class, () -> projectService.createProject(null, mockEmployee));
        assertThrows(InvalidActionException.class, () -> projectService.createProject(mockProject, null));

    }

    @Test
    public void whenProjectOrEmployeeIsNull_thenCannotUpdateProject() {
        assertThrows(InvalidActionException.class, () -> projectService.updateProject(null, mockEmployee));
        assertThrows(InvalidActionException.class, () -> projectService.updateProject(mockProject, null));
    }

    @Nested
    public class WhenEmployeeNotFound {
        @BeforeEach
        public void setup() {
            given(employeeRepo.getEmployeeById(mockEmployee.getId())).willReturn(Optional.empty());
        }

        @Test
        public void thenCannotCreateProject() {
            assertThrows(EntityNotFoundException.class, () -> projectService.createProject(mockProject, mockEmployee));
        }

        @Test
        public void thenCannotUpdateProject() {
            assertThrows(EntityNotFoundException.class, () -> projectService.updateProject(mockProject, mockEmployee));
        }
    }

    @Nested
    public class WhenEmployeeExists {
        @BeforeEach
        public void setup() {
            given(employeeRepo.getEmployeeById(mockEmployee.getId())).willReturn(Optional.of(mockEmployee));
        }

        @Nested
        public class whenDeveloper {
            @BeforeEach
            public void setup() {
                given(mockEmployee.getRole()).willReturn(Role.DEVELOPER);
            }

            @Test
            public void thenCannotCreateProject() {
                assertThrows(InvalidActionException.class, () -> projectService.createProject(mockProject, mockEmployee));
            }

            @Test
            public void thenCannotUpdateProject() {
                assertThrows(InvalidActionException.class, () -> projectService.updateProject(mockProject, mockEmployee));
            }
        }


        @Test
        public void whenProjectManager_thenCannotCreateProject() {
            given(mockEmployee.getRole()).willReturn(Role.PROJECTMANAGER);
            assertThrows(InvalidActionException.class, () -> projectService.createProject(mockProject, mockEmployee));
        }

        @Nested
        public class whenEmployeeIsAdministrator {
            @BeforeEach
            public void setUp() {
                given(mockEmployee.getRole()).willReturn(Role.ADMINISTRATOR);
            }

            @Test
            public void whenProjectCannotBeSavedToDatabase_thenThrowException() {
                given(projectRepo.saveProject(mockProject)).willReturn(Optional.empty());
                assertThrows(InternalException.class, () -> projectService.createProject(mockProject, mockEmployee));
            }


            @Nested
            public class whenExistingEmployeeAndExistingProject {
                @BeforeEach
                public void setup() {
                    given(projectRepo.saveProject(mockProject)).willReturn(Optional.of(mockProject));
                    given(employeeRepo.getEmployeeById(mockProject.getProjectManagerId())).willReturn(Optional.of(mockEmployee));
                }

                @Test
                public void whenValidProject_thenVerifyIfSaveIsCalled() {
                    projectService.createProject(mockProject, mockEmployee);
                    verify(projectRepo, times(1)).saveProject(mockProject);
                }
            }
        }
    }
}
