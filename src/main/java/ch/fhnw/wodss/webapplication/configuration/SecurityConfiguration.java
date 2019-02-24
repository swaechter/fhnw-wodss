package ch.fhnw.wodss.webapplication.configuration;

import ch.fhnw.wodss.webapplication.components.accounts.AccountUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AccountUserDetailsService accountUserDetailsService;

    public SecurityConfiguration(AccountUserDetailsService accountUserDetailsService) {
        this.accountUserDetailsService = accountUserDetailsService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationbuilder) throws Exception {
        // Provide a username/password based user detail service
        authenticationbuilder.userDetailsService(accountUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // Enable basic authentication
        // TODO: Force people to use this example with transport layer security-only
        httpSecurity
            .httpBasic();

        // Protect all pages except the entry page
        // TODO: User a hierarchical Spring role model
        httpSecurity
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/api/**").hasAnyRole("USER", "ADMIN")
            .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
            .antMatchers("/admin/**").hasRole("ADMIN")
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
            .deleteCookies("JSESSIONID");
    }
}
