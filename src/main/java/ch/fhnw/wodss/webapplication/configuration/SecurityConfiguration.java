package ch.fhnw.wodss.webapplication.configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /*private final EmployeeDetailsService employeeDetailsService;

    public SecurityConfiguration(EmployeeDetailsService employeeDetailsService) {
        this.employeeDetailsService = employeeDetailsService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationbuilder) throws Exception {
        // Provide a username/password based user detail service
        authenticationbuilder.userDetailsService(employeeDetailsService);
    }*/

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        /*// Enable basic authentication
        // TODO: Force people to use this example with transport layer security-only
        httpSecurity
            .httpBasic();

        // Protect all pages except the entry page
        // TODO: User a hierarchical Spring role model
        httpSecurity
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/api/**").hasAnyRole("ADMINISTRATOR", "PROJECTMANAGER", "DEVELOPER")
            .anyRequest().authenticated();

        // Enable CSRF: Write the CSRF token a client-side accessible cookie, but also accept it via header value. For more information see:
        // Tutorial: https://jaxenter.de/angular-csrf-spring-boot-52223
        // Official doc: https://docs.spring.io/spring-security/site/docs/5.0.x/reference/html/csrf.html#csrf-cookie
        // Official Javadoc: https://docs.spring.io/spring-security/site/docs/5.0.x/api/index.html?org/springframework/security/web/csrf/CookieCsrfTokenRepository.html
        httpSecurity.
            csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        // Provide a logout route to clear the session
        httpSecurity
            .logout()
            .logoutUrl("/logout") // Has to be triggered via POST
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID");*/
    }
}
