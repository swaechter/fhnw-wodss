package ch.fhnw.wodss.webapplication.components.employee;

import ch.fhnw.wodss.webapplication.utils.Converter;
import ch.fhnw.wodss.webapplication.utils.GenericCrudRepository;
import org.jooq.DSLContext;
import org.jooq.generated.tables.Employee;
import org.jooq.generated.tables.records.EmployeeRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeRepository extends GenericCrudRepository<EmployeeDto, EmployeeRecord, Employee> {

    public EmployeeRepository(DSLContext dslContext, Converter converter) {
        super(dslContext, converter, Employee.EMPLOYEE);
    }

    public Optional<EmployeeDto> saveEmployee(EmployeeDto employee) {
        return createOne(employee);
    }

    public List<EmployeeDto> getEmployees(Role role) {
        return readMany(table -> table.ID.isNotNull());
    }

    public Optional<EmployeeDto> getEmployeeById(Long id) {
        return readOne(table -> table.ID.eq(id));
    }

    public Optional<EmployeeDto> getEmployeeByEmailAddress(String emailAddress) {
        return readOne(table -> table.EMAIL_ADDRESS.eq(emailAddress));
    }

    public Optional<EmployeeDto> updateEmployee(EmployeeDto employee) {
        return updateOne(employee);
    }

    @Override
    protected EmployeeRecord mapDtoToRecord(EmployeeDto employee, EmployeeRecord employeeRecord) {
        if (employee.getId() != null) {
            employeeRecord.setId(employee.getId());
        }
        employeeRecord.setFirstName(employee.getFirstName());
        employeeRecord.setLastName(employee.getLastName());
        employeeRecord.setEmailAddress(employee.getEmailAddress());
        employeeRecord.setPasswordHash(employee.getPasswordHash());
        employeeRecord.setIsActive(employee.isActive());
        employeeRecord.setRole(getConverter().roleRecordToRoleDto(employee.getRole()));
        return employeeRecord;
    }

    @Override
    protected EmployeeDto mapRecordToDto(EmployeeRecord employeeRecord) {
        return getConverter().employeeRecordToEmployeeDto(employeeRecord);
    }
}

