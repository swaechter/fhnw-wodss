package ch.fhnw.wodss.webapplication.components.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(Employee employee, String password, Role role) {
        employee.setRole(role);
        return employeeRepository.saveEntry(employee);
    }

    public List<Employee> getEmployees(Role role) {
        return employeeRepository.getEntries();
    }

    public Employee getEmployee(long id) {
        return employeeRepository.getEntry(id);
    }

    public void updateEmployee(long id, Employee employee) {
        employeeRepository.updateEntry(id, employee);
    }

    public void anonymizeEmployee(long id) {
        Employee employee = getEmployee(id);
        employee.setFirstName("NONE");
        employee.setLastName("NONE");
        employee.setEmailAddress("NONE@NONE.NONE");
        employeeRepository.updateEntry(id, employee);
    }
}
