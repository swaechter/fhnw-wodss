package ch.fhnw.wodss.webapplication.components.project;

import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeRepository;
import ch.fhnw.wodss.webapplication.components.employee.Role;
import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import ch.fhnw.wodss.webapplication.exceptions.InvalidActionException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final EmployeeRepository employeeRepository;

    public ProjectService(ProjectRepository projectRepository, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    public ProjectDto createProject(ProjectDto project, AuthenticatedEmployee authenticatedEmployee) {
        abortIfNull(project, authenticatedEmployee);
        EmployeeDto employee = findEmployee(authenticatedEmployee.getId());
        abortIfNoPermission(employee);

        Optional<ProjectDto> createdProject = projectRepository.saveProject(project);

        if (createdProject.isEmpty()) {
            throw new InternalException("Unable to create the project");
        }

        return createdProject.get();
    }

    private void abortIfNull(ProjectDto project, AuthenticatedEmployee authenticatedEmployee) {
        if (project == null || authenticatedEmployee == null) {
            throw new IllegalArgumentException("Project or Employee must not be null");
        }
    }

    public List<ProjectDto> getProjects(LocalDate fromDate, LocalDate toDate, Long projectManagerId, AuthenticatedEmployee authenticatedEmployee) {
        return projectRepository.getProjects(fromDate, toDate, projectManagerId);
    }

    public ProjectDto getProject(Long id, AuthenticatedEmployee authenticatedEmployee) {
        Optional<ProjectDto> selectedProject = projectRepository.getProjectById(id);
        if (selectedProject.isEmpty()) {
            throw new EntityNotFoundException("project", id);
        }

        return selectedProject.get();
    }

    public ProjectDto updateProject(ProjectDto project, AuthenticatedEmployee authenticatedEmployee) {
        abortIfNull(project, authenticatedEmployee);

        EmployeeDto employee = findEmployee(project.getProjectManagerId());
        abortIfNoPermission(employee);

        Optional<ProjectDto> selectedProject = projectRepository.getProjectById(project.getId());
        if (selectedProject.isEmpty()) {
            throw new EntityNotFoundException("project", project.getId());
        }


        Optional<ProjectDto> updatedProject = projectRepository.updateProject(project);
        if (updatedProject.isEmpty()) {
            throw new InternalException("Unable to update the project");
        }

        return updatedProject.get();
    }

    private void abortIfNoPermission(EmployeeDto employee) {
        if (!employee.getRole().equals(Role.ADMINISTRATOR)) {
            throw new InvalidActionException("No permission to create a " + "project");
        }
    }

    private EmployeeDto findEmployee(Long employeeId) {
        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(employeeId);

        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", employeeId);
        }
        return selectedEmployee.get();
    }

    public void deleteProject(Long id, AuthenticatedEmployee authenticatedEmployee) {
        Optional<ProjectDto> selectedProject = projectRepository.getProjectById(id);
        if (selectedProject.isEmpty()) {
            throw new EntityNotFoundException("project", id);
        }

        projectRepository.deleteProject(id);
    }
}
