package ch.fhnw.wodss.webapplication.components.project;

import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import io.swagger.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/project", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Project", description = "Endpoint for managing all projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new project", nickname = "createProject")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "New project with the generated ID (ADMINISTRATOR)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to create a project (PROJECTMANAGER, DEVELOPER)"),
        @ApiResponse(code = 412, message = "Precondition for the project failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProjectDto> createProject(
        @Valid @RequestBody @ApiParam(value = "Project to create (The ID in the body will be ignored)", required = true) ProjectDto project,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        project.setId(null);
        project = projectService.createProject(project, authenticatedEmployee);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation(value = "Get all projects", nickname = "getProjects")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All projects (ADMINISTRATOR, PROJECTMANAGER) or only the involved ones (DEVELOPER)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 404, message = "Project manager not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<List<ProjectDto>> getProjects(
        @RequestParam(value = "projectManagerId", required = false) @ApiParam(value = "Filter the projects by a project manager ID", allowableValues = "range[1, 9223372036854775807]", example = "42", required = false) Long projectManagerId,
        @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "Start date (YYYY-MM-DD) to create a time range with a lower boundary (Projects with a start date before, but an end date after this date will match the criteria). Filters can stack", example = "2019-01-01", required = false) LocalDate fromDate,
        @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "End date (YYYY-MM-DD) to create a time range with an upper boundary (Projects with a start date before, but an end date after this date will match the criteria). Filters can stack", example = "2019-03-13", required = false) LocalDate toDate,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        List<ProjectDto> projects = projectService.getProjects(fromDate, toDate, projectManagerId, authenticatedEmployee);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a specific project", nickname = "getProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific project (ADMINISTRATOR, PROJECTMANAGER: All, DEVELOPER: Assigned projects)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to get the project (DEVELOPER: Not assigned project)"),
        @ApiResponse(code = 404, message = "Project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<ProjectDto> getProject(
        @PathVariable("id") @ApiParam(value = "ID of the project", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) @Min(1) @Max(Long.MAX_VALUE) Long id,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        ProjectDto project = projectService.getProject(id, authenticatedEmployee);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a specific project. If the end date is changed to an earlier date in the future (Before now/past is not possible), all future allocations will be deleted and the end of pending allocations will be set to the new project end (Represents a project end)", nickname = "updateProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated project and potentially updated/deleted project allocations in case of a project end date change (ADMINISTRATOR: All, PROJECTMANAGER: Own projects)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to update the project (PROJECTMANAGER: Somebody else's project, DEVELOPER: All"),
        @ApiResponse(code = 404, message = "Project or project manager not found"),
        @ApiResponse(code = 412, message = "Precondition for the project failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<ProjectDto> updateProject(
        @PathVariable("id") @ApiParam(value = "ID of the project to be updated", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) @Min(1) @Max(Long.MAX_VALUE) Long id,
        @Valid @RequestBody @ApiParam(value = "Updated project (The ID in the body will be ignored)", required = true) ProjectDto project,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        project.setId(id);
        project = projectService.updateProject(project, authenticatedEmployee);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a specific project including all associated allocations (Note: Cascading delete)", nickname = "deleteProject")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Project and allocations successfully deleted (ADMINISTRATOR)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to delete the project (PROJECTMANAGER, DEVELOPER)"),
        @ApiResponse(code = 404, message = "Project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProject(
        @PathVariable("id") @ApiParam(value = "ID of the project to be deleted", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) @Min(1) @Max(Long.MAX_VALUE) Long id,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        projectService.deleteProject(id, authenticatedEmployee);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
