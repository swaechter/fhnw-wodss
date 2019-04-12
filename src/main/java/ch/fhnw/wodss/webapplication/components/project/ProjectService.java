package ch.fhnw.wodss.webapplication.components.project;

import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeRepository;
import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
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
        if (project == null || authenticatedEmployee == null) {
            throw new IllegalArgumentException(
                "Project or Employee must not be null");
        }

        if (project.getProjectManagerId() != authenticatedEmployee.getId()) {
            throw new IllegalStateException("Not authorized to create project");
        }
        Optional<ProjectDto> createdProject = projectRepository.saveProject(project);
        if (createdProject.isEmpty()) {
            throw new InternalException("Unable to create the project");
        }
        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(project.getProjectManagerId());
        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", project.getProjectManagerId());
        }

        return createdProject.get();
    }

    public List<ProjectDto> getProjects(LocalDate fromDate, LocalDate toDate, Long projectManagerId,
            AuthenticatedEmployee authenticatedEmployee) {
        return projectRepository.getProjects(fromDate, toDate, projectManagerId);
    }

    public ProjectDto getProject(Long id, AuthenticatedEmployee authenticatedEmployee) {
        Optional<ProjectDto> selectedProject = projectRepository.getProjectById(id);
        if (selectedProject.isEmpty()) {
            throw new EntityNotFoundException("project", id);
        }

        return selectedProject.get();
    }

    public ProjectDto updateProject(Long id, ProjectDto project, AuthenticatedEmployee authenticatedEmployee) {
        Optional<ProjectDto> selectedProject = projectRepository.getProjectById(id);
        if (selectedProject.isEmpty()) {
            throw new EntityNotFoundException("project", id);
        }

        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(project.getProjectManagerId());
        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", project.getProjectManagerId());
        }

        Optional<ProjectDto> updatedProject = projectRepository.updateProject(id, project);
        if (updatedProject.isEmpty()) {
            throw new InternalException("Unable to update the project");
        }

        return updatedProject.get();
    }

    public void deleteProject(Long id, AuthenticatedEmployee authenticatedEmployee) {
        Optional<ProjectDto> selectedProject = projectRepository.getProjectById(id);
        if (selectedProject.isEmpty()) {
            throw new EntityNotFoundException("project", id);
        }

        projectRepository.deleteProject(id);
    }
}
