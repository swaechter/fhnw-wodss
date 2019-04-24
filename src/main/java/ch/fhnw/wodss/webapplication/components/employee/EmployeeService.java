package ch.fhnw.wodss.webapplication.components.employee;

import ch.fhnw.wodss.webapplication.components.token.Credentials;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InsufficientPermissionException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import ch.fhnw.wodss.webapplication.exceptions.InvalidActionException;
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
        if (authenticatedEmployee.isActive()) {
            throw new InvalidActionException("Only guests can register new employees");
        }

        Optional<EmployeeDto> existingEmployee = employeeRepository.getEmployeeByEmailAddress(employee.getEmailAddress());
        if (existingEmployee.isPresent()) {
            throw new InvalidActionException("The given email address is already in use");
        }

        employee.setActive(false);
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

    public Optional<EmployeeDto> getEmployeeByEmailAddress(String emailAddress) {
        return employeeRepository.getEmployeeByEmailAddress(emailAddress);
    }

    public List<EmployeeDto> getEmployees(Role role, EmployeeDto authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        return employeeRepository.getEmployees(role);
    }

    public EmployeeDto getEmployee(UUID id, EmployeeDto authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(id);
        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", id);
        }

        return selectedEmployee.get();
    }

    public EmployeeDto updateEmployee(EmployeeDto employee, EmployeeDto authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        if (authenticatedEmployee.getRole() != Role.ADMINISTRATOR) {
            throw new InsufficientPermissionException("Only administrators can update an employee");
        }

        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(employee.getId());
        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", employee.getId());
        }

        // Because the password hash & role are ready-only and not sent by the client, we have to set them again. Otherwise we would receive an SQL constraint violation
        employee.setPasswordHash(selectedEmployee.get().getPasswordHash());
        employee.setRole(selectedEmployee.get().getRole());

        Optional<EmployeeDto> existingEmployee = employeeRepository.getEmployeeByEmailAddress(employee.getEmailAddress());
        if (existingEmployee.isPresent() && !existingEmployee.get().getId().equals(employee.getId())) {
            throw new InvalidActionException("The given email address is already in use");
        }

        List<EmployeeDto> administratorEmployees = employeeRepository.getEmployees(Role.ADMINISTRATOR);
        if (administratorEmployees.size() <= 1 && employee.getRole() != Role.ADMINISTRATOR) {
            throw new InvalidActionException("It's not possible to downgrade the last administrator");
        }

        Optional<EmployeeDto> updatedEmployee = employeeRepository.updateEmployee(employee);
        if (updatedEmployee.isEmpty()) {
            throw new InternalException("Unable to update the employee");
        }

        return updatedEmployee.get();
    }

    public void anonymizeEmployee(UUID id, EmployeeDto authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        if (authenticatedEmployee.getRole() != Role.ADMINISTRATOR) {
            throw new InsufficientPermissionException("Only administrators can anonymize an employee");
        }

        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(id);
        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", id);
        }

        List<EmployeeDto> administratorEmployees = employeeRepository.getEmployees(Role.ADMINISTRATOR);
        if (administratorEmployees.size() <= 1) {
            throw new InvalidActionException("It's not possible to anonymize the last administrator");
        }

        EmployeeDto employee = selectedEmployee.get();
        employee.setFirstName("NONE");
        employee.setLastName("NONE");
        employee.setEmailAddress(UUID.randomUUID() + "@NONE.NONE");
        employee.setActive(false);
        employeeRepository.updateEmployee(employee);
    }
}
