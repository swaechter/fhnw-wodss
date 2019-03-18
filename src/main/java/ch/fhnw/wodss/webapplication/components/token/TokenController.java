package ch.fhnw.wodss.webapplication.components.token;

import ch.fhnw.wodss.webapplication.components.employee.Employee;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeService;
import ch.fhnw.wodss.webapplication.components.employee.Role;
import ch.fhnw.wodss.webapplication.configuration.TokenUtils;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/token", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = "Token", description = "Endpoint to manage your JWT token")
public class TokenController {

    private final EmployeeService employeeService;

    public TokenController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping(value = "")
    @ApiOperation(value = "Request a JWT token by an initial login process. The returned token contains the full employee, accessible via the claim employee", nickname = "requestToken")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "JWT token with the employee claim after a successful login"),
        @ApiResponse(code = 404, message = "Employee not found or invalid password"),
        @ApiResponse(code = 412, message = "Precondition for the username/password failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Token> requestToken(
        @Valid @RequestBody @ApiParam(value = "Credentials of the employee", required = true) Credentials credentials
    ) {
        if (credentials.getEmailAddress().equals("simon.waechter@students.fhnw.ch") && credentials.getRawPassword().equals("123456aA")) {
            Employee employee = new Employee("Simon", "Wächter", "simon.waechter@students.fhnw.ch", true, Role.ADMINISTRATOR);
            employee.setId(42L);

            Token token = TokenUtils.createTokenForEmployee(employee);
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("")
    @ApiOperation(value = "Request an updated/refreshed JWT token by providing the current valid one (Maybe soon expired?). The returned token contains the full employee, accessible via the claim employee", nickname = "refreshToken")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "New, updated JWT token after a successful refresh"),
        @ApiResponse(code = 401, message = "Unauthenticated or invalid token"),
        @ApiResponse(code = 404, message = "Token not valid or user not found"),
        @ApiResponse(code = 412, message = "Precondition for the token failed"),
        @ApiResponse(code = 500, message = "Uncaught or internal server error")
    })
    public ResponseEntity<Token> refreshToken(
        @RequestBody @ApiParam(value = "Valid token issued by our server", required = true) Token token
    ) {
        Optional<Employee> employee = TokenUtils.getEmployeeFromToken(token);
        if (employee.isPresent()) {
            token = TokenUtils.createTokenForEmployee(employee.get());
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
