package ch.fhnw.wodss.webapplication.components.contract;

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
        @ApiResponse(code = 201, message = "New contract with the generated ID (ADMINISTRATOR)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to create a contract (PROJECTMANAGER, DEVELOPER)"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 412, message = "Precondition for the contract failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ContractDto> createContract(
        @Valid @RequestBody @ApiParam(value = "Contract to create (The ID in the body will be ignored)", required = true) ContractDto contract,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        contract = contractService.createContract(contract, authenticatedEmployee);
        return new ResponseEntity<>(contract, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation(value = "Get all contracts", nickname = "getContracts")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All contracts (ADMINISTRATOR, PROJECTMANAGER) or only own ones (DEVELOPER)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<List<ContractDto>> getContracts(
        @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "Start date (YYYY-MM-DD) to create a time range with a lower boundary (Contracts with a start date before, but an end date after this date will match the criteria). Filters can stack", example = "2019-01-01", required = false) LocalDate fromDate,
        @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "End date (YYYY-MM-DD) to create a time range with an upper boundary (Contracts with a start date before, but an end date after this date will match the criteria). Filters can stack", example = "2019-03-13", required = false) LocalDate toDate,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        List<ContractDto> contacts = contractService.getContracts(fromDate, toDate, authenticatedEmployee);
        return new ResponseEntity<>(contacts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a specific contract", nickname = "getContract")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific contract (ADMINISTRATOR, PROJECTMANAGER: All, DEVELOPER: Own contracts)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to get the contract (DEVELOPER: Somebody's else's contract)"),
        @ApiResponse(code = 404, message = "Contract not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<ContractDto> getContract(
        @PathVariable("id") @ApiParam(value = "ID of the contract", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true) UUID id,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        ContractDto contract = contractService.getContract(id, authenticatedEmployee);
        return new ResponseEntity<>(contract, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a specific contract", nickname = "updateContract")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated contract (ADMINISTRATOR)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to update the contract (PROJECTMANAGER, DEVELOPER)"),
        @ApiResponse(code = 404, message = "Contract or employee not found"),
        @ApiResponse(code = 412, message = "Precondition for the contract failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<ContractDto> updateContract(
        @Valid @PathVariable("id") @ApiParam(value = "ID of the contract to be updated", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true) UUID id,
        @RequestBody @ApiParam(value = "Updated contract (The ID in the body will be ignored)", required = true) ContractDto contract,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        contract.setId(id);
        contract = contractService.updateContract(contract, authenticatedEmployee);
        return new ResponseEntity<>(contract, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a specific contract (Note: The contract can only be deleted as long as he is not used for an allocation)", nickname = "deleteContract")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Contract successfully deleted (ADMINISTRATOR)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to delete the contract (PROJECTMANAGER, DEVELOPER)"),
        @ApiResponse(code = 404, message = "Contract not found"),
        @ApiResponse(code = 412, message = "Precondition for the contract failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> anonymizeContract(
        @PathVariable("id") @ApiParam(value = "ID of the contract to be deleted", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true) UUID id,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        contractService.deleteContract(id, authenticatedEmployee);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
