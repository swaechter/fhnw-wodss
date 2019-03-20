package ch.fhnw.wodss.webapplication.components.employee;

import ch.fhnw.wodss.webapplication.utils.MemoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository extends MemoryRepository<EmployeeDto> {

    @Override
    public void setEntityId(EmployeeDto entry, Long value) {
        entry.setId(value);
    }
}
