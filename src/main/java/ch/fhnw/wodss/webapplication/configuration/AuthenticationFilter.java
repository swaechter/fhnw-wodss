package ch.fhnw.wodss.webapplication.configuration;

import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.token.Token;
import ch.fhnw.wodss.webapplication.components.token.TokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER_NAME = "Authorization";

    private static final String KEY_BEGINNING = "Bearer ";

    private final TokenService tokenService;

    public AuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader(HEADER_NAME);
        if (token != null && token.startsWith(KEY_BEGINNING)) {
            token = token.replace(KEY_BEGINNING, "");
            Optional<EmployeeDto> employee = tokenService.getEmployeeFromToken(new Token(token));
            if (employee.isPresent()) {
                EmployeeDto realEmployee = employee.get();
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(realEmployee.getEmailAddress(), token, new ArrayList<>()));
            }
        }
        /* else {
            SecurityContextHolder.clearContext();
        }*/

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
