package ch.fhnw.wodss.webapplication.components.employee;

import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/api/employee", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Employee", description = "Endpoint for managing all employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new employee", nickname = "createEmployee")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "New employee with the generated ID (EVERYONE - as a guest)"),
        @ApiResponse(code = 412, message = "Precondition for the employee failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EmployeeDto> createEmployee(
        @Valid @RequestBody @ApiParam(value = "Employee to create (The active flag in the body will be ignored)", required = true) EmployeeDto employee,
        @RequestParam(value = "password") @ApiParam(value = "Password of the new employee", required = true) String password,
        @RequestParam(value = "role") @ApiParam(value = "Role of the new employee", required = true) Role role,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        employee = employeeService.createEmployee(employee, password, role, authenticatedEmployee);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation(value = "Get all employees", nickname = "getEmployees")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All employees (ADMINISTRATOR, PROJECTMANAGER, DEVELOPER)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<List<EmployeeDto>> getEmployees(
        @RequestParam(value = "role", required = false) @ApiParam(value = "Filter the employees by role)", allowableValues = "ADMINISTRATOR, PROJECTMANAGER, DEVELOPER", example = "ADMINISTRATOR", required = false) Role role,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        List<EmployeeDto> employees = employeeService.getEmployees(role, authenticatedEmployee);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a specific employee", nickname = "getEmployee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific employee (ADMINISTRATOR, PROJECTMANAGER, DEVELOPER)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<EmployeeDto> getEmployee(
        @PathVariable("id") @ApiParam(value = "ID of the employee", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true) UUID id,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        EmployeeDto employee = employeeService.getEmployee(id, authenticatedEmployee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a specific employee", nickname = "updateEmployee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated employee (ADMINISTRATOR)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to update the employee (PROJECTMANAGER, DEVELOPER)"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 412, message = "Precondition for the employee failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<EmployeeDto> updateEmployee(
        @PathVariable("id") @ApiParam(value = "ID of the employee to be updated", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true) UUID id,
        @Valid @RequestBody @ApiParam(value = "Updated employee (The ID and role in the body will be ignored)", required = true) EmployeeDto employee,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        employee.setId(id);
        employeeService.updateEmployee(employee, authenticatedEmployee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Anonymize an employee (Note: No entities will be deleted)", nickname = "anonymizeEmployee")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Employee successfully anonymized (ADMINISTRATOR)"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 403, message = "Missing permission to anonymize the employee (PROJECTMANAGER, DEVELOPER)"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> anonymizeEmployee(
        @PathVariable("id") @ApiParam(value = "ID of the employee to be anonymized", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true) UUID id,
        AuthenticatedEmployee authenticatedEmployee
    ) {
        employeeService.anonymizeEmployee(id, authenticatedEmployee);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
