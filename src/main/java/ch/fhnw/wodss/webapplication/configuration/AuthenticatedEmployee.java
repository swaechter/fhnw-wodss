package ch.fhnw.wodss.webapplication.configuration;

import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.token.Token;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class AuthenticatedEmployee extends EmployeeDto implements Authentication {

    private final Token token;

    public AuthenticatedEmployee(EmployeeDto employee, Token token) {
        super(employee);
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public Object getCredentials() {
        return token.getToken();
    }

    @Override
    public Object getDetails() {
        return this;
    }

    @Override
    public Object getPrincipal() {
        return getEmailAddress();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // Do nothing
    }

    @Override
    public String getName() {
        return getEmailAddress();
    }
}
