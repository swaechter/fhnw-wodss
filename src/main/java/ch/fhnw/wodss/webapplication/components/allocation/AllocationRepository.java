package ch.fhnw.wodss.webapplication.components.allocation;

import ch.fhnw.wodss.webapplication.utils.Converter;
import ch.fhnw.wodss.webapplication.utils.GenericCrudRepository;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.generated.tables.Allocation;
import org.jooq.generated.tables.records.AllocationRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.generated.tables.Allocation.ALLOCATION;
import static org.jooq.generated.tables.Contract.CONTRACT;
import static org.jooq.generated.tables.Project.PROJECT;

@Repository
public class AllocationRepository extends GenericCrudRepository<AllocationDto, AllocationRecord, Allocation> {

    public AllocationRepository(DSLContext dslContext, Converter converter) {
        super(dslContext, converter, ALLOCATION);
    }

    public Optional<AllocationDto> saveAllocation(AllocationDto allocation) {
        allocation.setId(UUID.randomUUID());
        return createOne(allocation);
    }

    public List<AllocationDto> getAllocations(UUID projectManagerId, UUID employeeId, UUID projectId, LocalDate fromDate, LocalDate toDate) {
        // Be aware of overlapping start and end dates: https://stackoverflow.com/a/17014131
        Date startDate = getConverter().localDateToSqlDate(fromDate);
        Date endDate = getConverter().localDateToSqlDate(toDate);

        // Sadly we can't rely on our generic repository and have to access the DSL context directly
        SelectConditionStep<Record> condition = getDslContext().select().from(ALLOCATION)
            .join(PROJECT).on(ALLOCATION.PROJECT_ID.eq(PROJECT.ID))
            .join(CONTRACT).on(ALLOCATION.CONTRACT_ID.eq(CONTRACT.ID))
            .where(ALLOCATION.START_DATE.lessOrEqual(endDate)).and(ALLOCATION.END_DATE.greaterOrEqual(startDate))
            .and(projectManagerId != null ? PROJECT.PROJECT_MANAGER_ID.eq(projectManagerId) : DSL.noCondition())
            .and(employeeId != null ? CONTRACT.EMPLOYEE_ID.eq(employeeId) : DSL.noCondition())
            .and(projectId != null ? PROJECT.ID.eq(projectId) : DSL.noCondition());

        return getConverter().allocationRecordListToAllocationDtoList(condition.fetchInto(Allocation.ALLOCATION));
    }

    public List<AllocationDto> getAllocationsByContractId(UUID contractId) {
        return readMany(table -> table.CONTRACT_ID.eq(contractId));
    }

    public Optional<AllocationDto> getAllocationById(UUID id) {
        return readOne(table -> table.ID.eq(id));
    }

    public Optional<AllocationDto> updateAllocation(AllocationDto allocation) {
        return updateOne(allocation);
    }

    public void deleteAllocation(UUID id) {
        deleteMany(table -> table.ID.eq(id));
    }

    public void deleteAllocationsByProjectId(UUID projectId) {
        deleteMany(table -> table.PROJECT_ID.eq(projectId));
    }

    @Override
    protected AllocationRecord mapDtoToRecord(AllocationDto allocation, AllocationRecord allocationRecord) {
        if (allocation.getId() != null) {
            allocationRecord.setId(allocation.getId());
        }
        allocationRecord.setStartDate(getConverter().localDateToSqlDate(allocation.getStartDate()));
        allocationRecord.setEndDate(getConverter().localDateToSqlDate(allocation.getEndDate()));
        allocationRecord.setPensumPercentage(allocation.getPensumPercentage());
        allocationRecord.setContractId(allocation.getContractId());
        allocationRecord.setProjectId(allocation.getProjectId());
        return allocationRecord;
    }

    @Override
    protected AllocationDto mapRecordToDto(AllocationRecord allocationRecord) {
        return getConverter().allocationRecordToAllocationDto(allocationRecord);
    }
}
