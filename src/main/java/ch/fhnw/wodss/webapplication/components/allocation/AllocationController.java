package ch.fhnw.wodss.webapplication.components.allocation;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/allocation", produces = "application/json")
@Api(tags = "Allocation", description = "Endpoint for managing all allocations")
public class AllocationController {

    private final AllocationService allocationService;

    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new allocation", nickname = "createAllocation")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "New allocation with the generated ID"),
        @ApiResponse(code = 403, message = "Missing permission to create a allocation"),
        @ApiResponse(code = 404, message = "Contract or project not found"),
        @ApiResponse(code = 412, message = "Precondition for the allocation failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Allocation> createAllocation(
        @Valid @RequestBody @ApiParam(value = "Allocation to create (The ID in the body will be ignored)", required = true) Allocation allocation
    ) {
        allocation = allocationService.createAllocation(allocation);
        return new ResponseEntity<>(allocation, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation(value = "Get all allocations", nickname = "getAllocations")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All (filtered) allocations (Administrator) or only the own ones (Project Manager/Developer)"),
        @ApiResponse(code = 403, message = "Missing permission to get the allocations"),
        @ApiResponse(code = 404, message = "Employee, contract or project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public List<Allocation> getAllocations(
        @RequestParam(value = "employeeId", required = false) @ApiParam(value = "Filter the allocations by an employee (Each filter is exclusive, so filters do not stack)", allowableValues = "range[1, 9223372036854775807]", example = "42", required = false) Long employeeId,
        @RequestParam(value = "contractId", required = false) @ApiParam(value = "Filter the allocations by a contract of an employee (Each filter is exclusive, so filters do not stack))", allowableValues = "range[1, 9223372036854775807]", example = "42", required = false) Long contractId,
        @RequestParam(value = "projectId", required = false) @ApiParam(value = "Filter the allocations by a project (Each filter is exclusive, so filters do not stack))", allowableValues = "range[1, 9223372036854775807]", example = "42", required = false) Long projectId
    ) {
        return allocationService.getAllocations(employeeId, contractId, projectId);
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
        @PathVariable("id") @ApiParam(value = "ID of the allocation", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id
    ) {
        Allocation allocation = allocationService.getAllocation(id);
        return new ResponseEntity<>(allocation, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a specific allocation", nickname = "updateAllocation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated allocation"),
        @ApiResponse(code = 403, message = "Missing permission to update the allocation"),
        @ApiResponse(code = 404, message = "Allocation, contract or project not found"),
        @ApiResponse(code = 412, message = "Precondition for the allocation failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Allocation> updateAllocation(
        @PathVariable("id") @ApiParam(value = "ID of the allocation to be updated", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id,
        @Valid @RequestBody @ApiParam(value = "Updated allocation (The ID in the body will be ignored)", required = true) Allocation allocation
    ) {
        allocationService.updateAllocation(id, allocation);
        return new ResponseEntity<>(allocation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a specific allocation", nickname = "deleteAllocation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Allocation successfully deleted"),
        @ApiResponse(code = 403, message = "Missing permission to delete the allocation"),
        @ApiResponse(code = 404, message = "Allocation not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Void> deleteAllocation(
        @PathVariable("id") @ApiParam(value = "ID of the allocation to be deleted", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id
    ) {
        allocationService.deleteAllocation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
