package ch.fhnw.wodss.webapplication.components.project;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    public Project createProject(Project project) {
        project.setId(42L);
        return project;
    }

    public List<Project> getProjects(LocalDate fromDate, LocalDate toDate) {
        return new ArrayList<>();
    }

    public Project getProject(Long id) {
        return new Project();
    }

    public void updateProject(Long id, Project newProject) {
    }

    public void deleteProject(Long id) {
    }
}
