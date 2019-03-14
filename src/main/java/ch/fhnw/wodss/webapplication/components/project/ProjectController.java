package ch.fhnw.wodss.webapplication.components.project;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/project", consumes = "application/json", produces = "application/json")
@Api(tags = "Project", description = "Endpoint for managing all projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new project", nickname = "createProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New project with the generated ID"),
        @ApiResponse(code = 403, message = "Missing permission to create a project"),
        @ApiResponse(code = 412, message = "Precondition for the project failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
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
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public List<Project> getProjects(
        @RequestParam(value = "includePast", required = false) @ApiParam(value = "Flag to include previous, already finished/ended projects", allowableValues = "true, false", example = "true", required = false) boolean includePast
    ) {
        return projectService.getProjects(includePast);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a specific project", nickname = "getProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific project"),
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
    @ApiOperation(value = "Delete a specific project including all associated allocations", nickname = "deleteProject")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Project successfully deleted"),
        @ApiResponse(code = 403, message = "Missing permission to delete the project"),
        @ApiResponse(code = 404, message = "Project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Void> deleteProject(
        @PathVariable("id") @ApiParam(value = "ID of the project to be deleted", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id
    ) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
