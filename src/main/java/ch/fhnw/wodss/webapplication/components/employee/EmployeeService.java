package ch.fhnw.wodss.webapplication.components.employee;

import ch.fhnw.wodss.webapplication.components.token.Credentials;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public EmployeeDto createEmployee(EmployeeDto employee, String password, Role role, EmployeeDto authenticatedEmployee) {
        employee.setRole(role);
        employee.setPasswordHash(passwordEncoder.encode(password));

        Optional<EmployeeDto> createdEmployee = employeeRepository.saveEmployee(employee);
        if (createdEmployee.isEmpty()) {
            throw new InternalException("Unable to create the employee");
        }

        return createdEmployee.get();
    }

    public Optional<EmployeeDto> getEmployeeFromLogon(Credentials credentials) {
        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeByEmailAddress(credentials.getEmailAddress());
        if (selectedEmployee.isEmpty()) {
            return Optional.empty();
        }

        return passwordEncoder.matches(credentials.getRawPassword(), selectedEmployee.get().getPasswordHash()) ? selectedEmployee : Optional.empty();
    }

    public List<EmployeeDto> getEmployees(Role role, EmployeeDto authenticatedEmployee) {
        return employeeRepository.getEmployees(role);
    }

    public EmployeeDto getEmployee(UUID id, EmployeeDto authenticatedEmployee) {
        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(id);
        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", id);
        }

        return selectedEmployee.get();
    }

    public EmployeeDto updateEmployee(EmployeeDto employee, EmployeeDto authenticatedEmployee) {
        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(employee.getId());
        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", employee.getId());
        }

        Optional<EmployeeDto> updatedEmployee = employeeRepository.updateEmployee(employee);
        if (updatedEmployee.isEmpty()) {
            throw new InternalException("Unable to update the employee");
        }

        return updatedEmployee.get();
    }

    public void anonymizeEmployee(UUID id, EmployeeDto authenticatedEmployee) {
        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(id);
        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", id);
        }

        EmployeeDto employee = selectedEmployee.get();
        employee.setFirstName("NONE");
        employee.setLastName("NONE");
        employee.setEmailAddress(UUID.randomUUID() + "@NONE.NONE");
        employeeRepository.updateEmployee(employee);
    }
}
