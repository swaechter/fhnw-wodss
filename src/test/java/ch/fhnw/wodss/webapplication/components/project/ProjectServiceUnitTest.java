package ch.fhnw.wodss.webapplication.components.project;


import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeRepository;
import ch.fhnw.wodss.webapplication.components.employee.Role;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InsufficientPermissionException;
import ch.fhnw.wodss.webapplication.exceptions.InvalidActionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceUnitTest {
    @Mock
    private ProjectRepository projectRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    private EmployeeDto validEmployee = new EmployeeDto();

    private ProjectDto validProject = new ProjectDto();

    private ProjectService projectService;

    @BeforeEach
    public void setUp() {
        projectService = new ProjectService(projectRepo, employeeRepo);
        validEmployee.setId(UUID.randomUUID());
        validEmployee.setRole(Role.ADMINISTRATOR);
        validEmployee.setActive(true);
        validEmployee.setEmailAddress("test@fhnw.ch");
        validEmployee.setFirstName("John");
        validEmployee.setLastName("Doe");
        validEmployee.setPasswordHash("My secret PW");

        validProject.setName("Test project");
        validProject.setStartDate(LocalDate.now());
        validProject.setEndDate(LocalDate.now().plusYears(1));
        validProject.setId(UUID.randomUUID());
        validProject.setProjectManagerId(UUID.randomUUID());
    }

    @Nested
    public class WhenEmployeeNotFound {
        @BeforeEach
        public void setup() {
            given(employeeRepo.getEmployeeById(validEmployee.getId())).willReturn(Optional.empty());
        }

        @Test
        public void thenCannotCreateProject() {
            Exception ex = assertThrows(EntityNotFoundException.class, () -> projectService.createProject(validProject, validEmployee));
            assertEquals(ex.getMessage(), EntityNotFoundException.message("employee", validEmployee.getId()));
        }

        @Test
        public void thenCannotUpdateProject() {
            Exception ex = assertThrows(EntityNotFoundException.class, () -> projectService.updateProject(validProject, validEmployee));
            assertEquals(ex.getMessage(), EntityNotFoundException.message("employee", validEmployee.getId()));
        }

        @Test
        public void thenCannotReadSingleProject() {
            Exception ex = assertThrows(EntityNotFoundException.class, () -> projectService.getProject(validProject.getId(), validEmployee));
            assertEquals(ex.getMessage(), EntityNotFoundException.message("employee", validEmployee.getId()));
        }

        @Test
        public void thenCannotReadAllProject() {
            Exception ex = assertThrows(EntityNotFoundException.class, () -> projectService.getProjects(LocalDate.now(), LocalDate.now().plusYears(1), validProject.getProjectManagerId(), validEmployee));
            assertEquals(ex.getMessage(), EntityNotFoundException.message("employee", validEmployee.getId()));
        }

        @Test
        public void thenCannotDeleteProject() {
            Exception ex = assertThrows(EntityNotFoundException.class, () -> projectService.deleteProject(validProject.getId(), validEmployee));
            assertEquals(ex.getMessage(), EntityNotFoundException.message("employee", validEmployee.getId()));
        }
    }

    @Nested
    public class WhenEmployeeExists {
        @BeforeEach
        public void setup() {
            given(employeeRepo.getEmployeeById(validEmployee.getId())).willReturn(Optional.of(validEmployee));
        }

        @Nested
        public class WhenEmployeeIsNotActive {
            @BeforeEach
            public void setup() {
                validEmployee.setActive(false);
            }

            @Test
            public void thenCannotCreateProject() {
                Exception ex = assertThrows(InvalidActionException.class, () -> projectService.createProject(validProject, validEmployee));
                assertEquals(ex.getMessage(), ProjectService.EMPLOYEE_NOT_ACTIVATED);
            }

            @Test
            public void thenCannotUpdateProject() {
                Exception ex = assertThrows(InvalidActionException.class, () -> projectService.updateProject(validProject, validEmployee));
                assertEquals(ex.getMessage(), ProjectService.EMPLOYEE_NOT_ACTIVATED);
            }

            @Test
            public void thenCannotReadSingleProject() {
                Exception ex = assertThrows(InvalidActionException.class, () -> projectService.getProject(validProject.getId(), validEmployee));
                assertEquals(ex.getMessage(), ProjectService.EMPLOYEE_NOT_ACTIVATED);
            }

            @Test
            public void thenCannotReadAllProject() {
                Exception ex = assertThrows(InvalidActionException.class, () -> projectService.getProjects(LocalDate.now(), LocalDate.now().plusYears(1), validProject.getProjectManagerId(), validEmployee));
                assertEquals(ex.getMessage(), ProjectService.EMPLOYEE_NOT_ACTIVATED);
            }

            @Test
            public void thenCannotDeleteProject() {
                Exception ex = assertThrows(InvalidActionException.class, () -> projectService.deleteProject(validProject.getId(), validEmployee));
                assertEquals(ex.getMessage(), ProjectService.EMPLOYEE_NOT_ACTIVATED);
            }
        }

        @Nested
        public class whenProjectIsNotFound {
            @BeforeEach
            public void setup() {
                given(projectRepo.getProjectById(validProject.getId())).willReturn(Optional.empty());
            }

            @Test
            public void thenCannotReadSingleProject() {
                Exception ex = assertThrows(EntityNotFoundException.class, () -> projectService.getProject(validProject.getId(), validEmployee));
                assertEquals(ex.getMessage(), EntityNotFoundException.message("project", validProject.getId()));
            }

            @Test
            public void thenCannotUpdateProject() {
                Exception ex = assertThrows(EntityNotFoundException.class, () -> projectService.updateProject(validProject, validEmployee));
                assertEquals(ex.getMessage(), EntityNotFoundException.message("project", validProject.getId()));
            }

            @Test
            public void thenCannotDeleteProject() {
                Exception ex = assertThrows(EntityNotFoundException.class, () -> projectService.deleteProject(validProject.getId(), validEmployee));
                assertEquals(ex.getMessage(), EntityNotFoundException.message("project", validProject.getId()));
            }
        }

        @Nested
        public class whenDeveloper {
            @BeforeEach
            public void setup() {
                validEmployee.setRole(Role.DEVELOPER);
            }

            @Test
            public void thenCannotCreateProject() {
                Exception ex = assertThrows(InsufficientPermissionException.class, () -> projectService.createProject(validProject, validEmployee));
                assertEquals(ex.getMessage(), ProjectService.NO_PERMISSION_TO_CREATE_PROJECT);
            }

            @Test
            public void thenCannotUpdateProject() {
                Exception ex = assertThrows(InsufficientPermissionException.class, () -> projectService.updateProject(validProject, validEmployee));
                assertEquals(ex.getMessage(), ProjectService.NO_PERMISSION_TO_UPDATE_PROJECT);
            }

            @Test
            public void thenCannotDeleteProject() {
                Exception ex = assertThrows(InsufficientPermissionException.class, () -> projectService.deleteProject(validProject.getId(), validEmployee));
                assertEquals(ex.getMessage(), ProjectService.NO_PERMISSION_TO_DELETE_PROJECT);
            }

            @Nested
            public class whenProjectIsFound {
                @BeforeEach
                public void setup() {
                    given(projectRepo.getProjectById(validProject.getId())).willReturn(Optional.of(validProject));
                }

                @Test
                public void whenNotAssignedOnSelectedProject_thenCannotReadSingleProject() {
                    given(projectRepo.getProjectIfAssigned(validProject.getId(), validEmployee.getId())).willReturn(Optional.empty());
                    Exception ex = assertThrows(InsufficientPermissionException.class, () -> projectService.getProject(validProject.getId(), validEmployee));
                    assertEquals(ex.getMessage(), ProjectService.NO_PERMISSION_TO_READ_PROJECT);
                }

                @Test
                public void whenAssignedOnSelectedProject_thenCanReadSingleProject() {
                    given(projectRepo.getProjectIfAssigned(validProject.getId(), validEmployee.getId())).willReturn(Optional.of(validProject));
                    projectService.getProject(validProject.getId(), validEmployee);
                    verify(projectRepo, times(1)).getProjectIfAssigned(validProject.getId(), validEmployee.getId());
                }
            }

            @Test
            public void thenCanReadAllProjectsWhereAssignedTo() {
                given(projectRepo.getAllAssignedProjects(LocalDate.now(), LocalDate.now().plusYears(1), validEmployee.getId())).willReturn(Collections.emptyList());

                projectService.getProjects(LocalDate.now(), LocalDate.now().plusYears(1), validProject.getProjectManagerId(), validEmployee);

                verify(projectRepo, times(1)).getAllAssignedProjects(LocalDate.now(), LocalDate.now().plusYears(1), validEmployee.getId());
            }
        }

        @Nested
        public class whenProjectManager {
            @BeforeEach
            public void setup() {
                validEmployee.setRole(Role.PROJECTMANAGER);
            }

            @Test
            public void thenCannotCreateProject() {
                Exception ex = assertThrows(InsufficientPermissionException.class, () -> projectService.createProject(validProject, validEmployee));

                assertEquals(ex.getMessage(), ProjectService.NO_PERMISSION_TO_CREATE_PROJECT);
            }

            @Test
            public void thenCannotDeleteProject() {
                Exception ex = assertThrows(InsufficientPermissionException.class, () -> projectService.deleteProject(validProject.getId(), validEmployee));

                assertEquals(ex.getMessage(), ProjectService.NO_PERMISSION_TO_DELETE_PROJECT);
            }

            @Nested
            public class whenProjectIsFound {
                @BeforeEach
                public void setup() {
                    given(projectRepo.getProjectById(validProject.getId())).willReturn(Optional.of(validProject));
                }

                @Test
                public void whenNotProjectManagerOfSelectedProject_thenCannotUpdateProject() {
                    validProject.setProjectManagerId(UUID.randomUUID());
                    Exception ex = assertThrows(InsufficientPermissionException.class, () -> projectService.updateProject(validProject, validEmployee));
                    assertEquals(ex.getMessage(), ProjectService.NO_PERMISSION_TO_UPDATE_PROJECT);
                }

                @Test
                public void whenProjectmanagerOfSelectedProject_thenCanUpdateProject() {
                    validProject.setProjectManagerId(validEmployee.getId());
                    given(projectRepo.updateProject(validProject)).willReturn(Optional.of(validProject));

                    projectService.updateProject(validProject, validEmployee);
                    verify(projectRepo, times(1)).updateProject(validProject);
                }

                @Test
                public void thenCanReadSingleProject() {
                    projectService.getProject(validProject.getId(), validEmployee);
                    // 2 times, once to verify that it exists and once to
                    // get it
                    verify(projectRepo, times(2)).getProjectById(validProject.getId());
                }
            }

            @Test
            public void thenCanReadAllProjects() {
                projectService.getProjects(LocalDate.now(), LocalDate.now().plusYears(1), validEmployee.getId(), validEmployee);
                verify(projectRepo, times(1)).getProjects(LocalDate.now(), LocalDate.now().plusYears(1), validEmployee.getId());
            }
        }
    }

    @Nested
    public class whenAdministrator {
        @BeforeEach
        public void setup() {
            validEmployee.setRole(Role.ADMINISTRATOR);
            lenient().when(employeeRepo.getEmployeeById(validEmployee.getId())).thenReturn(Optional.of(validEmployee));

        }

        @Test
        public void thenCanReadAllProjects() {
            given(projectRepo.getProjects(LocalDate.now(), LocalDate.now().plusYears(1), validEmployee.getId())).willReturn(List.of(validProject));
            projectService.getProjects(LocalDate.now(), LocalDate.now().plusYears(1), validProject.getProjectManagerId(), validEmployee);
            verify(projectRepo, times(1)).getProjects(LocalDate.now(), LocalDate.now().plusYears(1), validEmployee.getId());

        }

        @Nested
        public class whenProjectExists {
            @BeforeEach
            public void setup() {
                given(projectRepo.getProjectById(validProject.getId())).willReturn(Optional.of(validProject));
            }

            @Test
            public void thenCanDeleteProject() {
                projectService.deleteProject(validProject.getId(), validEmployee);
                verify(projectRepo, times(1)).deleteProject(validProject.getId());
            }

            @Test
            public void thenCanReadSingleProject() {
                projectService.getProject(validProject.getId(), validEmployee);
                verify(projectRepo, times(2)).getProjectById(validProject.getId());
                // 2 times, once to verify that it exists and once to
                // get it
            }

            @Test
            public void whenProjectManagerIsNotFound_thenCannotUpdateProject() {
                when(employeeRepo.getEmployeeById(validProject.getProjectManagerId())).thenReturn(Optional.empty());

                Exception ex = assertThrows(EntityNotFoundException.class, () -> projectService.updateProject(validProject, validEmployee));
                assertEquals(ex.getMessage(), EntityNotFoundException.message("employee", validProject.getProjectManagerId()));
            }
        }


        @Test
        public void whenProjectManagerIsNotFound_thenCannotCreateProject() {
            when(employeeRepo.getEmployeeById(validProject.getProjectManagerId())).thenReturn(Optional.empty());

            Exception ex = assertThrows(EntityNotFoundException.class, () -> projectService.createProject(validProject, validEmployee));
            assertEquals(ex.getMessage(), EntityNotFoundException.message("employee", validProject.getProjectManagerId()));
        }

        @Nested
        public class whenProjectHasAnExistingProjectManager {
            private EmployeeDto projectManager;

            @BeforeEach
            public void setup() {
                projectManager = new EmployeeDto(validEmployee);
                projectManager.setId(UUID.randomUUID());
                validProject.setProjectManagerId(projectManager.getId());
                when(employeeRepo.getEmployeeById(validProject.getProjectManagerId())).thenReturn(Optional.of(projectManager));
            }

            @Test
            public void whenProjectManagerIsDeveloper_thenCannotCreateProject() {
                projectManager.setRole(Role.DEVELOPER);
                Exception ex = assertThrows(InvalidActionException.class, () -> projectService.createProject(validProject, validEmployee));
                assertEquals(ex.getMessage(), ProjectService.PROJECT_MANAGER_IS_DEVELOPER);
            }

            @Test
            public void whenProjectManagerIsDeveloper_thenCannotUpdateProject() {
                given(projectRepo.getProjectById(validProject.getId())).willReturn(Optional.of(validProject));
                projectManager.setRole(Role.DEVELOPER);
                Exception ex = assertThrows(InvalidActionException.class, () -> projectService.updateProject(validProject, validEmployee));
                assertEquals(ex.getMessage(), ProjectService.PROJECT_MANAGER_IS_DEVELOPER);
            }

            @Test
            public void whenProjectManagerIsProjectManagerOrAdministrator_thenCanUpdateProject() {
                given(projectRepo.getProjectById(validProject.getId())).willReturn(Optional.of(validProject));
                given(projectRepo.updateProject(validProject)).willReturn(Optional.of(validProject));
                projectManager.setRole(Role.PROJECTMANAGER);
                projectService.updateProject(validProject, validEmployee);
                verify(projectRepo, times(1)).updateProject(validProject);
            }

            @Test
            public void whenProjectAlreadyExists_thenCannotCreateProject() {
                projectManager.setRole(Role.PROJECTMANAGER);
                given(projectRepo.getProjectById(validProject.getId())).willReturn(Optional.of(validProject));

                Exception ex = assertThrows(InvalidActionException.class, () -> projectService.createProject(validProject, validEmployee));
                assertEquals(ex.getMessage(), ProjectService.PROJECT_ALREADY_EXISTS);
            }

            @Test
            public void whenProjectManagerAndProjectDoesNotYetExist_thenCanCreateProject() {
                projectManager.setRole(Role.PROJECTMANAGER);
                given(projectRepo.getProjectById(validProject.getId())).willReturn(Optional.empty());
                given(projectRepo.saveProject(validProject)).willReturn(Optional.of(validProject));
                projectService.createProject(validProject, validEmployee);
                verify(projectRepo, times(1)).saveProject(validProject);
            }
        }
    }
}
