package ch.fhnw.wodss.webapplication.components.contract;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController

@RequestMapping(value = "/api/contract", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Contract", description = "Endpoint for managing all contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new contract", nickname = "createContract")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "New contract with the generated ID"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to create a contract"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 412, message = "Precondition for the contract failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Contract> createContract(
        @RequestBody @ApiParam(value = "Contract to create (The ID in the body will be ignored)", required = true) Contract contract
    ) {
        contract = contractService.createContract(contract);
        return new ResponseEntity<>(contract, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation(value = "Get all contracts", nickname = "getContracts")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All contracts (Administrator/Project Manager) or only own ones (Developer)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public List<Contract> getContracts(
        @RequestParam(value = "fromDate", required = false) @ApiParam(value = "Start date (YYYY-MM-DD) to create a time range with a lower boundary (Contracts with a start date before, but an end date after this date will match the criteria). Filters can stack", example = "2019-01-01", required = false) LocalDate fromDate,
        @RequestParam(value = "toDate", required = false) @ApiParam(value = "End date (YYYY-MM-DD) to create a time range with an upper boundary (Contracts with a start date before, but an end date after this date will match the criteria). Filters can stack", example = "2019-03-13", required = false) LocalDate toDate
    ) {
        return contractService.getContracts(fromDate, toDate);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a specific contract", nickname = "getContract")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific contract"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to get the contract"),
        @ApiResponse(code = 404, message = "Contract not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Contract> getContract(
        @PathVariable("id") @ApiParam(value = "ID of the contract", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id
    ) {
        Contract contract = contractService.getContract(id);
        return new ResponseEntity<>(contract, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a specific contract", nickname = "updateContract")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated contract"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to update the contract"),
        @ApiResponse(code = 404, message = "Contract or employee not found"),
        @ApiResponse(code = 412, message = "Precondition for the contract failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Contract> updateContract(
        @PathVariable("id") @ApiParam(value = "ID of the contract to be updated", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id,
        @RequestBody @ApiParam(value = "Updated contract (The ID in the body will be ignored)", required = true) Contract contract
    ) {
        contractService.updateContract(id, contract);
        return new ResponseEntity<>(contract, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a specific contract (Note: The contract can only be deleted as long as he is not used for an allocation)", nickname = "deleteContract")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Contract successfully deleted"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to delete the contract"),
        @ApiResponse(code = 404, message = "Contract not found"),
        @ApiResponse(code = 412, message = "Precondition for the contract failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> anonymizeContract(
        @PathVariable("id") @ApiParam(value = "ID of the contract to be deleted", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id
    ) {
        contractService.deleteContract(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
