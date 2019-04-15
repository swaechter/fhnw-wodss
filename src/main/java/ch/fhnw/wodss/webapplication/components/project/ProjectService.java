package ch.fhnw.wodss.webapplication.components.project;

import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;

import java.time.LocalDate;
import java.util.List;

public interface ProjectService {

    ProjectDto createProject(
        ProjectDto project,
        AuthenticatedEmployee authenticatedEmployee);

    List<ProjectDto> getProjects(
        LocalDate fromDate,
        LocalDate toDate,
        Long projectManagerId,
        AuthenticatedEmployee authenticatedEmployee);

    ProjectDto getProject(
        Long id,
        AuthenticatedEmployee authenticatedEmployee);

    ProjectDto updateProject(
        Long id,
        ProjectDto project,
        AuthenticatedEmployee authenticatedEmployee);

    void deleteProject(
        Long id,
        AuthenticatedEmployee authenticatedEmployee);
}
