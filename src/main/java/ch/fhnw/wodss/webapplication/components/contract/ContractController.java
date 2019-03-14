package ch.fhnw.wodss.webapplication.components.contract;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/contract", produces = "application/json")
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
        @ApiResponse(code = 403, message = "Missing permission to create a contract"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 412, message = "Precondition for the contract failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Contract> createContract(
        @Valid @RequestBody @ApiParam(value = "Contract to create (The ID in the body will be ignored)", required = true) Contract contract
    ) {
        contract = contractService.createContract(contract);
        return new ResponseEntity<>(contract, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation(value = "Get all contracts", nickname = "getContracts")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All contracts (Administrator/Project Manager) or only own ones (Developer)"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public List<Contract> getContracts() {
        return contractService.getContracts();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a specific contract", nickname = "getContract")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific contract"),
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
        @ApiResponse(code = 403, message = "Missing permission to update the contract"),
        @ApiResponse(code = 404, message = "Contract or employee not found"),
        @ApiResponse(code = 412, message = "Precondition for the contract failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Contract> updateContract(
        @PathVariable("id") @ApiParam(value = "ID of the contract to be updated", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id,
        @Valid @RequestBody @ApiParam(value = "Updated contract (The ID in the body will be ignored)", required = true) Contract contract
    ) {
        contractService.updateContract(id, contract);
        return new ResponseEntity<>(contract, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a specific contract including all associated allocations", nickname = "deleteContract")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Contract successfully deleted"),
        @ApiResponse(code = 403, message = "Missing permission to delete the contract"),
        @ApiResponse(code = 404, message = "Contract not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Void> deleteContract(
        @PathVariable("id") @ApiParam(value = "ID of the contract to be deleted", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id
    ) {
        contractService.deleteContract(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
