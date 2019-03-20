package ch.fhnw.wodss.webapplication.components.project;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectDto createProject(ProjectDto project) {
        return projectRepository.saveEntry(project);
    }

    public List<ProjectDto> getProjects(LocalDate fromDate, LocalDate toDate, Long projectManagerId) {
        return projectRepository.getEntries();
    }

    public ProjectDto getProject(Long id) {
        return projectRepository.getEntry(id);
    }

    public void updateProject(Long id, ProjectDto newProject) {
        projectRepository.updateEntry(id, newProject);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteEntry(id);
    }
}
