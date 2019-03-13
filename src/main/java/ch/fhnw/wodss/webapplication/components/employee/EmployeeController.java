package ch.fhnw.wodss.webapplication.components.employee;

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
@RequestMapping("/api/employee")
@Api(tags = "Employee", description = "Endpoint for managing all employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("")
    @ApiOperation("Create a new employee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New employee with the generated ID"),
        @ApiResponse(code = 403, message = "Missing permission to create an employee"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Employee> createEmployee(
        @Valid @RequestBody Employee employee
    ) {
        employee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(employee, HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation("Get all employees")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All employees (Administrator/Project Manager/Developer)"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get a specific employee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific employee"),
        @ApiResponse(code = 403, message = "Missing permission to get this employee"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Employee> getEmployee(
        @PathVariable("id") long id
    ) {
        Employee employee = employeeService.getEmployee(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update a specific employee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Specific updated employee"),
        @ApiResponse(code = 403, message = "Missing permission to update this employee"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Employee> updateEmployee(
        @PathVariable("id") long id,
        @Valid @RequestBody Employee employee
    ) {
        employeeService.updateEmployee(id, employee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete a specific employee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Employee successfully deleted"),
        @ApiResponse(code = 403, message = "Missing permission to delete this employee"),
        @ApiResponse(code = 404, message = "Employee not found"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Void> deleteEmployee(
        @PathVariable("id") long id
    ) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
