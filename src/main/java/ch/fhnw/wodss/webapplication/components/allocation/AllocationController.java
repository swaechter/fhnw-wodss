package ch.fhnw.wodss.webapplication.components.allocation;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/allocation", consumes = "application/json", produces = "application/json")
@Api(tags = "Allocation", description = "Endpoint for managing all allocations")
public class AllocationController {

    private final AllocationService allocationService;

    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new allocation", nickname = "createAllocation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New allocation with the generated ID"),
        @ApiResponse(code = 403, message = "Missing permission to create a allocation"),
        @ApiResponse(code = 404, message = "Employee or project not found"),
        @ApiResponse(code = 412, message = "Precondition for the allocation failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Allocation> createAllocation(
        @Valid @RequestBody @ApiParam("Allocation to create (The ID in the body will be ignored)") Allocation allocation
    ) {
        allocation = allocationService.createAllocation(allocation);
        return new ResponseEntity<>(allocation, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation(value = "Get all allocations", nickname = "getAllocations")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All allocations (Administrator) or only the own ones (Project Manager/Developer)"),
        @ApiResponse(code = 404, message = "Employee or project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public List<Allocation> getAllocations(
        @RequestParam(value = "employeeId", required = false) @ApiParam("Filter the allocations by an employee (Multiple filters can stack)") Long employeeId,
        @RequestParam(value = "projectId", required = false) @ApiParam("Filter the allocations by a project (Multiple filters can stack)") Long projectId
    ) {
        return allocationService.getAllocations(employeeId, projectId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a specific allocation", nickname = "getAllocation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific allocation"),
        @ApiResponse(code = 403, message = "Missing permission to get the allocation"),
        @ApiResponse(code = 404, message = "Allocation not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Allocation> getAllocation(
        @PathVariable("id") @ApiParam("ID of the allocation") long id
    ) {
        Allocation allocation = allocationService.getAllocation(id);
        return new ResponseEntity<>(allocation, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a specific allocation", nickname = "updateAllocation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated allocation"),
        @ApiResponse(code = 403, message = "Missing permission to update the allocation"),
        @ApiResponse(code = 404, message = "Allocation, employee or project not found"),
        @ApiResponse(code = 412, message = "Precondition for the allocation failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Allocation> updateAllocation(
        @PathVariable("id") @ApiParam("ID of the allocation to be updated") Long id,
        @Valid @RequestBody @ApiParam("Updated allocation (The ID in the body will be ignored)") Allocation allocation
    ) {
        allocationService.updateAllocation(id, allocation);
        return new ResponseEntity<>(allocation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a specific allocation including all associated allocations", nickname = "deleteAllocation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Allocation successfully deleted"),
        @ApiResponse(code = 403, message = "Missing permission to delete the allocation"),
        @ApiResponse(code = 404, message = "Allocation not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Void> deleteAllocation(
        @PathVariable("id") @ApiParam("ID of the allocation to be deleted") long id
    ) {
        allocationService.deleteAllocation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
