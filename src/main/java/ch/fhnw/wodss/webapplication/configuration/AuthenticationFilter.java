package ch.fhnw.wodss.webapplication.configuration;

import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeService;
import ch.fhnw.wodss.webapplication.components.token.Token;
import ch.fhnw.wodss.webapplication.components.token.TokenService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "Authorization";

    private static final String KEY_BEGINNING = "Bearer ";

    private final TokenService tokenService;

    private final EmployeeService employeeService;

    public AuthenticationFilter(TokenService tokenService, EmployeeService employeeService) {
        this.tokenService = tokenService;
        this.employeeService = employeeService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String rawToken = httpServletRequest.getHeader(HEADER_NAME);
        if (rawToken != null && rawToken.startsWith(KEY_BEGINNING)) {
            Token token = new Token(rawToken.replace(KEY_BEGINNING, ""));
            Optional<EmployeeDto> tokenEmployee = tokenService.getEmployeeFromToken(token);

            // Manually recheck the token employee in the database. Not really the idea of a stateless & signed token like JWT...but simple
            tokenEmployee.ifPresent(tokenEmployeeDto -> {
                Optional<EmployeeDto> databaseEmployee = employeeService.getEmployeeByEmailAddress(tokenEmployeeDto.getEmailAddress());
                databaseEmployee.ifPresent(employeeDto -> SecurityContextHolder.getContext().setAuthentication(new AuthenticatedEmployee(employeeDto, token)));
            });
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
