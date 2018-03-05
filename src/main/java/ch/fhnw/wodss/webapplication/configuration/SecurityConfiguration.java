package ch.fhnw.wodss.webapplication.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
        // NOTE: Use an encrypted transport layer or just fuck off.....
        httpSecurity
            .httpBasic();

        // Protect all pages except the entry page
        httpSecurity
            .authorizeRequests()
            .antMatchers("/").permitAll()
            .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
            .antMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated();

        // Provide a logout route to clear the session
        httpSecurity
            .logout()
            .logoutUrl("/logout")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID");

        // Use session fixation for secure HTTP to HTTPS rewrite
        // NOTE: https://en.wikipedia.org/wiki/Session_fixation
        httpSecurity
            .sessionManagement()
            .sessionFixation()
            .newSession();
    }
}
