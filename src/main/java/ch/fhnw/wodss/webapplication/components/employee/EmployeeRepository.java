package ch.fhnw.wodss.webapplication.components.employee;

import ch.fhnw.wodss.webapplication.utils.MemoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository extends MemoryRepository<Employee> {

    @Override
    public void setEntityId(Employee entry, Long value) {
        entry.setId(value);
    }
}
