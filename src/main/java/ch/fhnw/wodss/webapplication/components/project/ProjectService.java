package ch.fhnw.wodss.webapplication.components.project;

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

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectDto createProject(ProjectDto project, AuthenticatedEmployee authenticatedEmployee) {
        Optional<ProjectDto> createdProject = projectRepository.saveProject(project);
        if (createdProject.isEmpty()) {
            throw new InternalException("Unable to create the project");
        }

        return createdProject.get();
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

    public ProjectDto updateProject(Long id, ProjectDto project, AuthenticatedEmployee authenticatedEmployee) {
        Optional<ProjectDto> updatedProject = projectRepository.updateProject(id, project);
        if (updatedProject.isEmpty()) {
            throw new EntityNotFoundException("project", id);
        }

        return updatedProject.get();
    }

    public void deleteProject(Long id, AuthenticatedEmployee authenticatedEmployee) {
        projectRepository.deleteProject(id);
    }
}
