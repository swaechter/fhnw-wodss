package ch.fhnw.wodss.webapplication.components.contract;

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
@RequestMapping("/api/contract")
@Api(tags = "Contract", description = "Endpoint for managing all contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("")
    @ApiOperation("Create a new contract")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New contract with the generated ID"),
        @ApiResponse(code = 403, message = "Missing permission to create a contract"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Contract> createContract(
        @Valid @RequestBody Contract contract
    ) {
        contract = contractService.createContract(contract);
        return new ResponseEntity<>(contract, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation("Get all contracts")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All contracts (Administrator/Project Manager) or only own ones (Developer)"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public List<Contract> getContracts() {
        return contractService.getContracts();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get a specific contract")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific contract"),
        @ApiResponse(code = 403, message = "Missing permission to get this contract"),
        @ApiResponse(code = 404, message = "Contract not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Contract> getContract(
        @PathVariable("id") long id
    ) {
        Contract contract = contractService.getContract(id);
        return new ResponseEntity<>(contract, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update a specific contract")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated contract"),
        @ApiResponse(code = 403, message = "Missing permission to update this contract"),
        @ApiResponse(code = 404, message = "Contract or employee not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Contract> updateContract(
        @PathVariable("id") long id,
        @Valid @RequestBody Contract contract
    ) {
        contractService.updateContract(id, contract);
        return new ResponseEntity<>(contract, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a specific contract including all associated allocations")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Contract successfully deleted"),
        @ApiResponse(code = 403, message = "Missing permission to delete this contract"),
        @ApiResponse(code = 404, message = "Contract not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Void> deleteContract(
        @PathVariable("id") long id
    ) {
        contractService.deleteContract(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
