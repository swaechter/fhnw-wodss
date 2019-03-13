package ch.fhnw.wodss.webapplication.components.project;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/project")
@Api(tags = "Project", description = "Endpoint for managing all projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("")
    @ApiOperation("Create a new project")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New project with the generated ID"),
        @ApiResponse(code = 403, message = "Missing permission to create a project"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Project> createProject(
        @Valid @RequestBody Project project
    ) {
        project = projectService.createProject(project);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation("Get all projects")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All projects (Administrator) or only the assigned/involved ones (Project Manager/Developer)"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public List<Project> getProjects(
        @RequestParam(value = "includePast", required = false) boolean includePast
    ) {
        return projectService.getProjects(includePast);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get a specific project")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific project"),
        @ApiResponse(code = 403, message = "Missing permission to get this project"),
        @ApiResponse(code = 404, message = "Project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Project> getProject(
        @PathVariable("id") long id
    ) {
        Project project = projectService.getProject(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update a specific project")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated project"),
        @ApiResponse(code = 403, message = "Missing permission to update this project"),
        @ApiResponse(code = 404, message = "Project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Project> updateProject(
        @PathVariable("id") long id,
        @Valid @RequestBody Project project
    ) {
        projectService.updateProject(id, project);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a specific project including all associated allocations")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Project successfully deleted"),
        @ApiResponse(code = 403, message = "Missing permission to delete this project"),
        @ApiResponse(code = 404, message = "Project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Void> deleteProject(
        @PathVariable("id") long id
    ) {
        projectService.deleteProject(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
