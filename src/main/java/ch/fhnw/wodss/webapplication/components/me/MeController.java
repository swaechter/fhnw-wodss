package ch.fhnw.wodss.webapplication.components.me;

import ch.fhnw.wodss.webapplication.components.employee.Employee;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
@Api(tags = "Me", description = "Endpoint to get to know yourself")
public class MeController {

    private final EmployeeService employeeService;

    public MeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("")
    @ApiOperation("Get to know yourself as employee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "You, the employee"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Employee> getYourself() {
        Employee employee = employeeService.getEmployee(42L);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }
}
