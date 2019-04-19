package ch.fhnw.wodss.webapplication.components.contract;

import ch.fhnw.wodss.webapplication.utils.Converter;
import ch.fhnw.wodss.webapplication.utils.GenericCrudRepository;
import org.jooq.DSLContext;
import org.jooq.generated.tables.Contract;
import org.jooq.generated.tables.records.ContractRecord;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ContractRepository extends GenericCrudRepository<ContractDto, ContractRecord, Contract> {

    public ContractRepository(DSLContext dslContext, Converter converter) {
        super(dslContext, converter, Contract.CONTRACT);
    }

    public Optional<ContractDto> saveContract(ContractDto contract) {
        contract.setId(UUID.randomUUID());
        return createOne(contract);
    }

    public List<ContractDto> getContracts(LocalDate fromDate, LocalDate toDate, UUID employeeId) {
        // Be aware of overlapping start and end dates: https://stackoverflow.com/a/17014131
        Date startDate = getConverter().localDateToSqlDate(fromDate);
        Date endDate = getConverter().localDateToSqlDate(toDate);

        // Show only the employee contract or all contracts during the time period?
        if (employeeId != null) {
            return readMany(table -> table.EMPLOYEE_ID.eq(employeeId).and(table.START_DATE.lessOrEqual(endDate).and(table.END_DATE.greaterOrEqual(startDate))));
        } else {
            return readMany(table -> table.START_DATE.lessOrEqual(endDate).and(table.END_DATE.greaterOrEqual(startDate)));
        }
    }

    public Optional<ContractDto> getContractById(UUID id) {
        return readOne(table -> table.ID.eq(id));
    }

    public Optional<ContractDto> updateContract(ContractDto contract) {
        return updateOne(contract);
    }

    public void deleteContract(UUID id) {
        deleteOne(table -> table.ID.eq(id));
    }

    @Override
    protected ContractRecord mapDtoToRecord(ContractDto contract, ContractRecord contractRecord) {
        if (contract.getId() != null) {
            contractRecord.setId(contract.getId());
        }
        contractRecord.setStartDate(getConverter().localDateToSqlDate(contract.getStartDate()));
        contractRecord.setEndDate(getConverter().localDateToSqlDate(contract.getEndDate()));
        contractRecord.setPensumPercentage(contract.getPensumPercentage());
        contractRecord.setEmployeeId(contract.getEmployeeId());
        return contractRecord;
    }

    @Override
    protected ContractDto mapRecordToDto(ContractRecord contractRecord) {
        return getConverter().contractRecordToContractDto(contractRecord);
    }
}
