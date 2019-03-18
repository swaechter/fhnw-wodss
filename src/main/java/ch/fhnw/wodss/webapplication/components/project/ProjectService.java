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

    public Project createProject(Project project) {
        return projectRepository.saveEntry(project);
    }

    public List<Project> getProjects(LocalDate fromDate, LocalDate toDate, Long projectManagerId) {
        return projectRepository.getEntries();
    }

    public Project getProject(Long id) {
        return projectRepository.getEntry(id);
    }

    public void updateProject(Long id, Project newProject) {
        projectRepository.updateEntry(id, newProject);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteEntry(id);
    }
}
