package ch.fhnw.wodss.webapplication.components.employee;

import ch.fhnw.wodss.webapplication.components.project.ProjectDto;
import ch.fhnw.wodss.webapplication.components.project.ProjectRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public enum Role {

    ADMINISTRATOR {
        public Optional<ProjectDto> getSingleProjectAccordingToPermission(ProjectRepository projectRepo, UUID projectId, UUID employeeId) {
            return projectRepo.getProjectById(projectId);

        }

        @Override
        public List<ProjectDto> getAllProjectsAccordingToPermission(ProjectRepository projectRepo, LocalDate fromDate, LocalDate toDate, UUID employeeId) {
            return projectRepo.getProjects(fromDate, toDate, employeeId);
        }
    }, PROJECTMANAGER {
        public Optional<ProjectDto> getSingleProjectAccordingToPermission(ProjectRepository projectRepo, UUID projectId, UUID employeeId) {
            return projectRepo.getProjectById(projectId);
        }

        @Override
        public List<ProjectDto> getAllProjectsAccordingToPermission(ProjectRepository projectRepo, LocalDate fromDate, LocalDate toDate, UUID employeeId) {
            return projectRepo.getProjects(fromDate, toDate, employeeId);
        }
    }, DEVELOPER {
        public Optional<ProjectDto> getSingleProjectAccordingToPermission(ProjectRepository projectRepo, UUID projectId, UUID employeeId) {
            return projectRepo.getProjectIfAssigned(projectId, employeeId);
        }

        @Override
        public List<ProjectDto> getAllProjectsAccordingToPermission(ProjectRepository projectRepo, LocalDate fromDate, LocalDate toDate, UUID employeeId) {
            return projectRepo.getAllAssignedProjects(fromDate, toDate, employeeId);
        }
    };

    public abstract Optional<ProjectDto> getSingleProjectAccordingToPermission(ProjectRepository projectRepo, UUID projectId, UUID employeeId);

    public abstract List<ProjectDto> getAllProjectsAccordingToPermission(ProjectRepository projectRepo, LocalDate fromDate, LocalDate toDate, UUID employeeId);
}
