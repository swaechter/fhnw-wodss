package ch.fhnw.wodss.webapplication.components.project;

import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeRepository;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InsufficientPermissionException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import ch.fhnw.wodss.webapplication.exceptions.InvalidActionException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ProjectService {

    public static final String NO_PERMISSION_TO_CREATE_PROJECT = "No permission to create a " + "project";
    public static final String NO_PERMISSION_TO_UPDATE_PROJECT = "No permission to update a " + "project";
    public static final String EMPLOYEE_NOT_ACTIVATED = "The authenticated employee is " + "not activated";
    public static final String NO_PERMISSION_TO_DELETE_PROJECT = "No permission to delete a " + "project";
    public static final String NO_PERMISSION_TO_READ_PROJECT = "No permission to get a " + "project";
    public static final String PROJECT_MANAGER_IS_DEVELOPER = "The given project manager is a " + "developer";
    public static final String PROJECT_ALREADY_EXISTS = "Project already exists";

    private final ProjectRepository projectRepository;

    private final EmployeeRepository employeeRepository;

    public ProjectService(ProjectRepository projectRepository, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    public ProjectDto createProject(ProjectDto project, EmployeeDto employee) {
        EmployeeDto found = employeeRepository.getEmployeeById(employee.getId()).orElseThrow(() -> new EntityNotFoundException("employee", employee.getId()));
        if (!found.isActive()) {
            throw new InvalidActionException(EMPLOYEE_NOT_ACTIVATED);
        }

        if (!found.isAdministrator()) {
            throw new InsufficientPermissionException(NO_PERMISSION_TO_CREATE_PROJECT);
        }

        EmployeeDto projectManager = employeeRepository.getEmployeeById(project.getProjectManagerId()).orElseThrow(() -> new EntityNotFoundException("employee", project.getProjectManagerId()));
        if (projectManager.isDeveloper()) {
            throw new InvalidActionException(PROJECT_MANAGER_IS_DEVELOPER);
        }

        projectRepository.getProjectById(project.getId()).ifPresent((__) -> {
            throw new InvalidActionException(PROJECT_ALREADY_EXISTS);
        });
        return projectRepository.saveProject(project).orElseThrow(() -> new InternalException("Unable to create the project"));
    }

    public List<ProjectDto> getProjects(LocalDate fromDate, LocalDate toDate, UUID projectManagerId, EmployeeDto employee) {
        EmployeeDto found = employeeRepository.getEmployeeById(employee.getId()).orElseThrow(() -> new EntityNotFoundException("employee", employee.getId()));
        if (!found.isActive()) {
            throw new InvalidActionException(EMPLOYEE_NOT_ACTIVATED);
        }

        return found.getRole().getAllProjectsAccordingToPermission(projectRepository, fromDate, toDate, employee.getId());
    }

    public ProjectDto getProject(UUID id, EmployeeDto employee) {
        EmployeeDto foundEmployee = employeeRepository.getEmployeeById(employee.getId()).orElseThrow(() -> new EntityNotFoundException("employee", employee.getId()));
        if (!foundEmployee.isActive()) {
            throw new InvalidActionException(EMPLOYEE_NOT_ACTIVATED);
        }

        projectRepository.getProjectById(id).orElseThrow(() -> new EntityNotFoundException("project", id));
        return foundEmployee.getRole().getSingleProjectAccordingToPermission(projectRepository, id, employee.getId()).orElseThrow(() -> new InsufficientPermissionException(NO_PERMISSION_TO_READ_PROJECT));
    }


    public ProjectDto updateProject(ProjectDto project, EmployeeDto employee) {
        EmployeeDto foundEmployee = employeeRepository.getEmployeeById(employee.getId()).orElseThrow(() -> new EntityNotFoundException("employee", employee.getId()));
        if (!foundEmployee.isActive()) {
            throw new InvalidActionException(EMPLOYEE_NOT_ACTIVATED);
        }

        if (foundEmployee.isDeveloper()) {
            throw new InsufficientPermissionException(NO_PERMISSION_TO_UPDATE_PROJECT);
        }

        ProjectDto foundProject = projectRepository.getProjectById(project.getId()).orElseThrow(() -> new EntityNotFoundException("project", project.getId()));
        if (isNotProjectManagerOfSelectedProject(foundEmployee, foundProject)) {
            throw new InsufficientPermissionException(NO_PERMISSION_TO_UPDATE_PROJECT);
        }

        EmployeeDto projectManager = employeeRepository.getEmployeeById(project.getProjectManagerId()).orElseThrow(() -> new EntityNotFoundException("employee", project.getProjectManagerId()));

        if (projectManager.isDeveloper()) {
            throw new InvalidActionException(PROJECT_MANAGER_IS_DEVELOPER);
        }


        return projectRepository.updateProject(project).orElseThrow(() -> new InternalException("Unable to update the project"));
    }

    private boolean isNotProjectManagerOfSelectedProject(EmployeeDto foundEmployee, ProjectDto foundProject) {
        return foundEmployee.isProjectManager() && !foundProject.getProjectManagerId().equals(foundEmployee.getId());
    }


    public void deleteProject(UUID id, EmployeeDto employee) {
        EmployeeDto foundEmployee = employeeRepository.getEmployeeById(employee.getId()).orElseThrow(() -> new EntityNotFoundException("employee", employee.getId()));

        if (!foundEmployee.isActive()) {
            throw new InvalidActionException(EMPLOYEE_NOT_ACTIVATED);
        }

        if (!foundEmployee.isAdministrator()) {
            throw new InsufficientPermissionException(NO_PERMISSION_TO_DELETE_PROJECT);
        }

        projectRepository.getProjectById(id).orElseThrow(() -> new EntityNotFoundException("project", id));
        projectRepository.deleteProject(id);
    }
}
