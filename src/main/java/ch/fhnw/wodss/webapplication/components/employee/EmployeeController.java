package ch.fhnw.wodss.webapplication.components.employee;

import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/employee", produces = "application/json")
@Api(tags = "Employee", description = "Endpoint for managing all employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("")
    @ApiOperation(value = "Create a new employee", nickname = "createEmployee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New employee with the generated ID"),
        @ApiResponse(code = 403, message = "Missing permission to create an employee"),
        @ApiResponse(code = 412, message = "Precondition for the employee failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Employee> createEmployee(
        @Valid @RequestBody @ApiParam(value = "Employee to create (The ID in the body will be ignored)", required = true) Employee employee
    ) {
        employee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation(value = "Get all employees", nickname = "getEmployees")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All employees (Administrator/Project Manager/Developer)"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a specific employee", nickname = "getEmployee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific employee"),
        @ApiResponse(code = 403, message = "Missing permission to get the employee"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Employee> getEmployee(
        @PathVariable("id") @ApiParam(value = "ID of the employee", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id
    ) {
        Employee employee = employeeService.getEmployee(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a specific employee", nickname = "updateEmployee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated employee"),
        @ApiResponse(code = 403, message = "Missing permission to update the employee"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 412, message = "Precondition for the employee failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Employee> updateEmployee(
        @PathVariable("id") @ApiParam(value = "ID of the employee to be updated", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id,
        @Valid @RequestBody @ApiParam(value = "Updated employee (The ID in the body will be ignored)", required = true) Employee employee
    ) {
        employeeService.updateEmployee(id, employee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a specific employee", nickname = "deleteEmployee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Employee successfully deleted"),
        @ApiResponse(code = 403, message = "Missing permission to delete the employee"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Void> deleteEmployee(
        @PathVariable("id") @ApiParam(value = "ID of the employee to be deleted", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true) Long id
    ) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}