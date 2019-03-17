package ch.fhnw.wodss.webapplication.components.project;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/project", produces = "application/json")
@Api(tags = "Project", description = "Endpoint for managing all projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new project", nickname = "createProject")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "New project with the generated ID"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to create a project"),
        @ApiResponse(code = 412, message = "Precondition for the project failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Project> createProject(
        @Valid @RequestBody @ApiParam(value = "Project to create (The ID in the body will be ignored)", required = true) Project project
    ) {
        project = projectService.createProject(project);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation(value = "Get all projects", nickname = "getProjects")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All projects (Administrator) or only the assigned/involved ones (Project Manager/Developer)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public List<Project> getProjects(
        @RequestParam(value = "fromDate", required = false) @ApiParam(value = "Start date (YYYY-MM-DD) to create a time range with a lower boundary (Projects with a start date before, but an end date after this date will match the criteria)", example = "2019-01-01", required = false) LocalDate fromDate,
        @RequestParam(value = "toDate", required = false) @ApiParam(value = "End date (YYYY-MM-DD) to create a time range with an upper boundary (Projects with a start date before, but an end date after this date will match the criteria)", example = "2019-03-13", required = false) LocalDate toDate
    ) {
        return projectService.getProjects(fromDate, toDate);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a specific project", nickname = "getProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific project"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to get the project"),
        @ApiResponse(code = 404, message = "Project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Project> getProject(
        @PathVariable("id") @ApiParam(value = "ID of the project", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id
    ) {
        Project project = projectService.getProject(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a specific project", nickname = "updateProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated project"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to update the project"),
        @ApiResponse(code = 404, message = "Project not found"),
        @ApiResponse(code = 412, message = "Precondition for the project failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Project> updateProject(
        @PathVariable("id") @ApiParam(value = "ID of the project to be updated", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id,
        @Valid @RequestBody @ApiParam(value = "Updated project (The ID in the body will be ignored)", required = true) Project project
    ) {
        projectService.updateProject(id, project);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a specific project including all associated allocations (Note: Cascading delete)", nickname = "deleteProject")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Project and allocations successfully deleted"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to delete the project"),
        @ApiResponse(code = 404, message = "Project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProject(
        @PathVariable("id") @ApiParam(value = "ID of the project to be deleted", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id
    ) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
