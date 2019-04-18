package ch.fhnw.wodss.webapplication.components.allocation;

import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import io.swagger.annotations.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/allocation", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Allocation", description = "Endpoint for managing all allocations")
public class AllocationController {

    private final AllocationService allocationService;

    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new allocation", notes = "ADMINISTRATORS can create allocations for every project, PROJECTMANAGER for their own projects and DEVELOPER for none", nickname = "createAllocation")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "New allocation with the generated ID (ADMINISTRATOR: All, PROJECTMANAGER: Own projects)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to create an allocation (PROJECTMANAGER: Somebody's else's project, DEVELOPER: All)"),
        @ApiResponse(code = 404, message = "Contract or project not found"),
        @ApiResponse(code = 412, message = "Precondition for the allocation failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AllocationDto> createAllocation(
        @RequestBody @ApiParam(value = "Allocation to create (The ID in the body will be ignored)", required = true) AllocationDto allocation,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        allocation.setId(null);
        allocation = allocationService.createAllocation(allocation, authenticatedEmployee);
        return new ResponseEntity<>(allocation, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation(value = "Get all allocations", nickname = "getAllocations")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All (filtered) allocations (ADMINISTRATOR, PROJECTMANAGER) or only the employee own ones (DEVELOPER)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to get the allocation (DEVELOPER: Other uninvolved employee or project)"),
        @ApiResponse(code = 404, message = "Employee or project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<List<AllocationDto>> getAllocations(
        @RequestParam(value = "employeeId", required = false) @ApiParam(value = "Filter the allocations by an employee (Filters can stack)", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = false) UUID employeeId,
        @RequestParam(value = "projectId", required = false) @ApiParam(value = "Filter the allocations by a project (Filters can stack)", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = false) UUID projectId,
        @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "Start date (YYYY-MM-DD) to create a time range with a lower boundary (Allocations with a start date before, but an end date after this date will match the criteria). Filters can stack", example = "2019-01-01", required = false) LocalDate fromDate,
        @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "End date (YYYY-MM-DD) to create a time range with an upper boundary (Allocations with a start date before, but an end date after this date will match the criteria). Filters can stack", example = "2019-03-13", required = false) LocalDate toDate,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        List<AllocationDto> allocations = allocationService.getAllocations(employeeId, projectId, fromDate, toDate, authenticatedEmployee);
        return new ResponseEntity<>(allocations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a specific allocation", nickname = "getAllocation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific allocation (ADMINISTRATOR, PROJECTMANAGER: All, DEVELOPER: Assigned projects)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to get the allocation (DEVELOPER: Not assigned projects)"),
        @ApiResponse(code = 404, message = "Allocation not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<AllocationDto> getAllocation(
        @PathVariable("id") @ApiParam(value = "ID of the allocation", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true) UUID id,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        AllocationDto allocation = allocationService.getAllocation(id, authenticatedEmployee);
        return new ResponseEntity<>(allocation, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a specific allocation", nickname = "updateAllocation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated allocation (ADMINISTRATOR: All, PROJECTMANAGER: Own projects)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to update the allocation (PROJECTMANAGER: Somebody's else's project, DEVELOPER: ALL"),
        @ApiResponse(code = 404, message = "Allocation, contract or project not found"),
        @ApiResponse(code = 412, message = "Precondition for the allocation failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<AllocationDto> updateAllocation(
        @PathVariable("id") @ApiParam(value = "ID of the allocation to be updated", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true) UUID id,
        @Valid @RequestBody @ApiParam(value = "Updated allocation (The ID in the body will be ignored)", required = true) AllocationDto allocation,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        allocation.setId(id);
        allocation = allocationService.updateAllocation(allocation, authenticatedEmployee);
        return new ResponseEntity<>(allocation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a specific allocation", nickname = "deleteAllocation")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Allocation successfully deleted (ADMINISTRATOR: All, PROJECTMANAGER: Own projects)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to delete the allocation (PROJECTMANAGER: Somebody's else's project, DEVELOPER: ALL"),
        @ApiResponse(code = 404, message = "Allocation not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAllocation(
        @PathVariable("id") @ApiParam(value = "ID of the allocation to be deleted", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true) UUID id,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        allocationService.deleteAllocation(id, authenticatedEmployee);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
