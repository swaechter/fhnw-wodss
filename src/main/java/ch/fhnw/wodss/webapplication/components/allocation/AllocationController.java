package ch.fhnw.wodss.webapplication.components.allocation;

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
@RequestMapping("/api/allocation")
@Api(tags = "Allocation", description = "Endpoint for managing all allocations")

public class AllocationController {

    private final AllocationService allocationService;

    public AllocationController(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping("")
    @ApiOperation("Create a new allocation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New allocation with the generated ID"),
        @ApiResponse(code = 403, message = "Missing permission to create a allocation"),
        @ApiResponse(code = 404, message = "Contract or project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Allocation> createAllocation(
        @Valid @RequestBody Allocation allocation
    ) {
        allocation = allocationService.createAllocation(allocation);
        return new ResponseEntity<>(allocation, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation("Get all allocations")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All allocations (Administrator) or only the own ones (Project Manager/Developer)"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public List<Allocation> getAllocations(
        @RequestParam(value = "employeeId", required = false) Long employeeId,
        @RequestParam(value = "projectId", required = false) Long projectId
    ) {
        return allocationService.getAllocations(employeeId, projectId);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get a specific allocation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific allocation"),
        @ApiResponse(code = 403, message = "Missing permission to get this allocation"),
        @ApiResponse(code = 404, message = "Allocation not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Allocation> getAllocation(
        @PathVariable("id") long id
    ) {
        Allocation allocation = allocationService.getAllocation(id);
        return new ResponseEntity<>(allocation, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update a specific allocation")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated allocation"),
        @ApiResponse(code = 403, message = "Missing permission to update this allocation"),
        @ApiResponse(code = 404, message = "Allocation, contract or project not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Allocation> updateAllocation(
        @PathVariable("id") long id,
        @Valid @RequestBody Allocation allocation
    ) {
        allocationService.updateAllocation(id, allocation);
        return new ResponseEntity<>(allocation, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a specific allocation including all associated allocations")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Allocation successfully deleted"),
        @ApiResponse(code = 403, message = "Missing permission to delete this allocation"),
        @ApiResponse(code = 404, message = "Allocation not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Void> deleteAllocation(
        @PathVariable("id") long id
    ) {
        allocationService.deleteAllocation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
