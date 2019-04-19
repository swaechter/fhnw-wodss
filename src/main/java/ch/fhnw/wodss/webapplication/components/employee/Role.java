package ch.fhnw.wodss.webapplication.components.employee;

import ch.fhnw.wodss.webapplication.components.project.ProjectDto;
import ch.fhnw.wodss.webapplication.components.project.ProjectRepository;

import java.util.Optional;
import java.util.UUID;

public enum Role {
    ADMINISTRATOR
        {
            public Optional<ProjectDto> getSingleProjectAccordingToPermission(
                ProjectRepository projectRepo,
                UUID projectId)
            {
                return projectRepo.getProjectById(projectId);

            }
        }, PROJECTMANAGER
        {
            public Optional<ProjectDto> getSingleProjectAccordingToPermission(
                ProjectRepository projectRepo,
                UUID projectId)
            {
                return projectRepo.getProjectById(projectId);
            }
        }, DEVELOPER
        {
            public Optional<ProjectDto> getSingleProjectAccordingToPermission(
                ProjectRepository projectRepo,
                UUID projectId)
            {
                return projectRepo.getProjectIfAssigned(projectId);
            }
        };

    public abstract Optional<ProjectDto> getSingleProjectAccordingToPermission(
        ProjectRepository projectRepo,
        UUID projectId);
}
