package ch.fhnw.wodss.webapplication.components.employee;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    public Employee createEmployee(Employee employee) {
        employee.setId(42L);
        return employee;
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>();
    }

    public Employee getEmployee(long id) {
        return new Employee();
    }

    public void updateEmployee(long id, Employee employee) {
    }

    public void deleteEmployee(long id) {
    }
}
