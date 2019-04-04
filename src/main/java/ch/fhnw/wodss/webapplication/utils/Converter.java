package ch.fhnw.wodss.webapplication.utils;

import ch.fhnw.wodss.webapplication.components.allocation.AllocationDto;
import ch.fhnw.wodss.webapplication.components.contract.ContractDto;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.project.ProjectDto;
import org.jooq.generated.tables.records.AllocationRecord;
import org.jooq.generated.tables.records.ContractRecord;
import org.jooq.generated.tables.records.EmployeeRecord;
import org.jooq.generated.tables.records.ProjectRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface Converter {

    @Mappings({})
    List<AllocationDto> allocationRecordListToAllocationDtoList(List<AllocationRecord> allocationRecordList);

    @Mappings({})
    AllocationDto allocationRecordToAllocationDto(AllocationRecord allocationRecord);

    @Mappings({})
    List<ContractDto> contractRecordListToContractDtoList(List<ContractRecord> contractRecordList);

    @Mappings({})
    ContractDto contractRecordToContractDto(ContractRecord contractRecord);

    @Mappings({})
    List<EmployeeDto> employeeRecordListToEmployeeDtoList(List<EmployeeRecord> employeeRecordList);

    @Mappings({})
    EmployeeDto employeeRecordToEmployeeDto(EmployeeRecord employeeRecord);

    @Mappings({})
    List<ProjectDto> projectRecordListToProjectDtoList(List<ProjectRecord> projectRecordList);

    @Mappings({})
    ProjectDto projectRecordToProjectDto(ProjectRecord projectRecord);
}
