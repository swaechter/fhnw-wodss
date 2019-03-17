package ch.fhnw.wodss.webapplication.components.me;

import ch.fhnw.wodss.webapplication.components.employee.Employee;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/", produces = "application/json")
@Api(tags = "Me", description = "Endpoint to get to know yourself & manage your JWT tokens")
public class MeController {

    private final EmployeeService employeeService;

    public MeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/api/me")
    @ApiOperation(value = "Get to know yourself as employee", nickname = "getYourself")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "You, the employee"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Employee> getYourself() {
        Employee employee = employeeService.getEmployee(42L);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiOperation(value = "Request a JWT token by an initial login process. The JWT token is returned in the body", nickname = "requestToken")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "JWT token after a successful login"),
        @ApiResponse(code = 404, message = "Employee not found or invalid password"),
        @ApiResponse(code = 412, message = "Precondition for the username/password failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<String> requestToken(
        @RequestParam("email") @ApiParam(value = "Email address of the employee", required = true) String email,
        @RequestParam("password") @ApiParam(value = "Raw password of the employee", required = true) String password
    ) {
        if (email.equals("simon.waechter@students.fhnw.ch") && password.equals("123456aA")) {
            return new ResponseEntity<>("XXX", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "Request an updated/refreshed JWT token by providing the current valid one (Maybe soon expired?). The token is returned in the body", nickname = "refreshToken")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "New, updated JWT token after a successful refresh"),
        @ApiResponse(code = 404, message = "Token not valid or user not found"),
        @ApiResponse(code = 412, message = "Precondition for the token failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<String> refreshToken(
        @RequestParam("token") @ApiParam(value = "Valid token", required = true) String token
    ) {
        return new ResponseEntity<>("JWT Token - WIP: " + token, HttpStatus.OK);
    }
}
