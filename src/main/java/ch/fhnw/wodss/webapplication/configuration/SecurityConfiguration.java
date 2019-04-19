package ch.fhnw.wodss.webapplication.configuration;

import ch.fhnw.wodss.webapplication.components.employee.EmployeeService;
import ch.fhnw.wodss.webapplication.components.token.TokenService;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenService tokenService;

    private final EmployeeService employeeService;

    public SecurityConfiguration(TokenService tokenService, EmployeeService employeeService) {
        this.tokenService = tokenService;
        this.employeeService = employeeService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // Force Spring & Tomcat to never ever read or create a session - we want to be fully stateless
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // Disable CSRF protection because we don't rely on session/cookies (We just completely disabled them)
            .and().csrf().disable()

            // Send a 401 and not a 403 for unauthenticated users
            .exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))

            // Allow the /, /error, /api/employee and /api/token endpoints but enforce an authentication for the rest
            .and().authorizeRequests()
            .antMatchers(HttpMethod.GET, "/", "/error").permitAll()
            .antMatchers(HttpMethod.POST, "/api/employee**", "/api/token**").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "**").permitAll()
            .anyRequest().authenticated()

            // Add a filter to check the the JWT token and authenticate based on the token
            .and().addFilterBefore(new AuthenticationFilter(tokenService, employeeService), BasicAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Make Swagger accessible without a token
        web.ignoring().antMatchers(
            "/",
            "/csrf",
            "/v2/api-docs",
            "/swagger-resources/configuration/ui",
            "/configuration/ui",
            "/swagger-resources",
            "/swagger-resources/configuration/security",
            "/configuration/security",
            "/swagger-ui.html",
            "/index.html**",
            "/webjars/**",
            "/assets/***",
            "/**.js",
            "/**.css",
            "/**.png",
            "/**.ico",
            "/**.json");
    }
}
