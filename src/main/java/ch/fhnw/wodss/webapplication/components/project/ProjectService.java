package ch.fhnw.wodss.webapplication.components.project;

import ch.fhnw.wodss.webapplication.components.allocation.AllocationRepository;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeRepository;
import ch.fhnw.wodss.webapplication.components.employee.Role;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InsufficientPermissionException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import ch.fhnw.wodss.webapplication.exceptions.InvalidActionException;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ProjectService {

    public static final String NO_PERMISSION_TO_CREATE_PROJECT = "No permission to create a project";
    public static final String NO_PERMISSION_TO_UPDATE_PROJECT = "No permission to update a project";
    public static final String EMPLOYEE_NOT_ACTIVATED = "The authenticated employee is not activated";
    public static final String NO_PERMISSION_TO_DELETE_PROJECT = "No permission to delete a project";
    public static final String NO_PERMISSION_TO_READ_PROJECT = "No permission to get a project";
    public static final String PROJECT_MANAGER_IS_DEVELOPER = "The given project manager is a developer";
    public static final String PROJECT_END_DATE_BEFORE_START_DATE = "The end date of a project can't be after the project end date";
    public static final String PROJECT_NAME_IS_ALREADY_TAKEN = "The project name is already taken";

    private final ProjectRepository projectRepository;

    private final EmployeeRepository employeeRepository;

    private final AllocationRepository allocationRepository;

    public ProjectService(ProjectRepository projectRepository, EmployeeRepository employeeRepository, AllocationRepository allocationRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
        this.allocationRepository = allocationRepository;
    }

    public ProjectDto createProject(@Valid ProjectDto project, EmployeeDto employee) {
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

        // TODO@Thibo Test Case dafür schreiben
        if (project.getEndDate().isAfter(project.getEndDate())) {
            throw new InvalidActionException(PROJECT_END_DATE_BEFORE_START_DATE);
        }

        // TODO@Thibo: Half-done Falls Integration Test Issue resolved, noch einen Integration Test schreiben
        projectRepository.getProjectByName(project.getName()).ifPresent((__) -> {
            throw new InvalidActionException(PROJECT_NAME_IS_ALREADY_TAKEN);
        });

        return projectRepository.saveProject(project).orElseThrow(() -> new InternalException("Unable to create the project"));
    }

    public List<ProjectDto> getProjects(LocalDate fromDate, LocalDate toDate, UUID projectManagerId, EmployeeDto employee) {
        EmployeeDto found = employeeRepository.getEmployeeById(employee.getId()).orElseThrow(() -> new EntityNotFoundException("employee", employee.getId()));
        if (!found.isActive()) {
            throw new InvalidActionException(EMPLOYEE_NOT_ACTIVATED);
        }

        // Simulate a more or less "matching-everything" filter - otherwise we have to deal with 4 different scenarios in the repository
        fromDate = fromDate != null ? fromDate : LocalDate.of(1900, 1, 1);
        toDate = toDate != null ? toDate : LocalDate.of(2100, 1, 1);

        return getAllProjectsAccordingToPermission(found.getRole(), fromDate, toDate, projectManagerId, employee.getId());
    }

    public ProjectDto getProject(UUID id, EmployeeDto employee) {
        EmployeeDto foundEmployee = employeeRepository.getEmployeeById(employee.getId()).orElseThrow(() -> new EntityNotFoundException("employee", employee.getId()));
        if (!foundEmployee.isActive()) {
            throw new InvalidActionException(EMPLOYEE_NOT_ACTIVATED);
        }

        projectRepository.getProjectById(id).orElseThrow(() -> new EntityNotFoundException("project", id));
        return getSingleProjectAccordingToPermission(foundEmployee.getRole(), id, employee.getId()).orElseThrow(() -> new InsufficientPermissionException(NO_PERMISSION_TO_READ_PROJECT));
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

        // TODO@Thibo: Half-done Falls Integration Test Issue resolved, noch einen Integration Test schreiben
        projectRepository.getProjectByName(project.getName()).ifPresent((p) -> {
            if (!p.getId().equals(project.getId())) throw new InvalidActionException(PROJECT_NAME_IS_ALREADY_TAKEN);
        });

        EmployeeDto projectManager = employeeRepository.getEmployeeById(project.getProjectManagerId()).orElseThrow(() -> new EntityNotFoundException("employee", project.getProjectManagerId()));

        if (projectManager.isDeveloper()) {
            throw new InvalidActionException(PROJECT_MANAGER_IS_DEVELOPER);
        }

        // TODO@Thibo Test Case dafür schreiben
        if (project.getEndDate().isAfter(project.getEndDate())) {
            throw new InvalidActionException(PROJECT_END_DATE_BEFORE_START_DATE);
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
        allocationRepository.deleteAllocationsByProjectId(id);
        projectRepository.deleteProject(id);
    }

    private Optional<ProjectDto> getSingleProjectAccordingToPermission(Role role, UUID projectId, UUID employeeId) {
        if (role == Role.ADMINISTRATOR || role == Role.PROJECTMANAGER) {
            return projectRepository.getProjectById(projectId);
        } else {
            return projectRepository.getProjectIfAssigned(projectId, employeeId);
        }
    }

    private List<ProjectDto> getAllProjectsAccordingToPermission(Role role, LocalDate fromDate, LocalDate toDate, UUID projectManagerId, UUID employeeId) {
        if (role == Role.ADMINISTRATOR || role == Role.PROJECTMANAGER) {
            return projectRepository.getProjects(fromDate, toDate, projectManagerId);
        } else {
            return projectRepository.getAllAssignedProjects(fromDate, toDate, employeeId);
        }
    }
}
