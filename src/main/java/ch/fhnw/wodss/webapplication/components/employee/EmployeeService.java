package ch.fhnw.wodss.webapplication.components.employee;

import ch.fhnw.wodss.webapplication.components.token.Credentials;
import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public EmployeeDto createEmployee(EmployeeDto employee, String password, Role role, AuthenticatedEmployee authenticatedEmployee) {
        employee.setRole(role);
        employee.setTemporaryPasswordHash(passwordEncoder.encode(password)); // TODO: Remove this call - the password hash should be stored in the Jooq entity/database
        return employeeRepository.saveEntry(employee);
    }

    public Optional<EmployeeDto> getEmployeeFromLogon(Credentials credentials) {
        Optional<EmployeeDto> employee = employeeRepository.getEntries().stream().filter(employeeDto -> employeeDto.getEmailAddress().equals(credentials.getEmailAddress())).findFirst();
        if (employee.isEmpty()) {
            return Optional.empty();
        }

        return passwordEncoder.matches(credentials.getRawPassword(), employee.get().getTemporaryPasswordHash()) ? employee : Optional.empty();
    }

    public List<EmployeeDto> getEmployees(Role role, AuthenticatedEmployee authenticatedEmployee) {
        return employeeRepository.getEntries();
    }

    public EmployeeDto getEmployee(Long id, AuthenticatedEmployee authenticatedEmployee) {
        return employeeRepository.getEntry(id);
    }

    public void updateEmployee(Long id, EmployeeDto employee, AuthenticatedEmployee authenticatedEmployee) {
        employeeRepository.updateEntry(id, employee);
    }

    public void anonymizeEmployee(Long id, AuthenticatedEmployee authenticatedEmployee) {
        EmployeeDto employee = employeeRepository.getEntry(id);
        employee.setFirstName("NONE");
        employee.setLastName("NONE");
        employee.setEmailAddress("NONE@NONE.NONE");
        employeeRepository.updateEntry(id, employee);
    }
}
