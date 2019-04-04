package ch.fhnw.wodss.webapplication.components.project;

import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectDto createProject(ProjectDto project, AuthenticatedEmployee authenticatedEmployee) {
        return projectRepository.saveEntry(project);
    }

    public List<ProjectDto> getProjects(LocalDate fromDate, LocalDate toDate, Long projectManagerId, AuthenticatedEmployee authenticatedEmployee) {
        return projectRepository.getEntries();
    }

    public ProjectDto getProject(Long id, AuthenticatedEmployee authenticatedEmployee) {
        return projectRepository.getEntry(id);
    }

    public void updateProject(Long id, ProjectDto newProject, AuthenticatedEmployee authenticatedEmployee) {
        projectRepository.updateEntry(id, newProject);
    }

    public void deleteProject(Long id, AuthenticatedEmployee authenticatedEmployee) {
        projectRepository.deleteEntry(id);
    }
}
