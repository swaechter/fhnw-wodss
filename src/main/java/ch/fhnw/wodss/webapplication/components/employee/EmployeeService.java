package ch.fhnw.wodss.webapplication.components.employee;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    public Employee createEmployee(Employee employee, String password, Role role) {
        employee.setId(42L);
        employee.setRole(role);
        return employee;
    }

    public List<Employee> getEmployees(Role role) {
        return new ArrayList<>();
    }

    public Employee getEmployee(long id) {
        return new Employee();
    }

    public void updateEmployee(long id, Employee employee) {
    }

    public void anonymizeEmployee(long id) {
    }
}
