package ch.fhnw.wodss.webapplication.components.allocation;

import ch.fhnw.wodss.webapplication.utils.Converter;
import ch.fhnw.wodss.webapplication.utils.GenericCrudRepository;
import org.jooq.DSLContext;
import org.jooq.generated.tables.Allocation;
import org.jooq.generated.tables.records.AllocationRecord;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class AllocationRepository extends GenericCrudRepository<AllocationDto, AllocationRecord, Allocation> {

    public AllocationRepository(DSLContext dslContext, Converter converter) {
        super(dslContext, converter, Allocation.ALLOCATION);
    }

    public Optional<AllocationDto> saveAllocation(AllocationDto allocation) {
        return createOne(allocation);
    }

    public List<AllocationDto> getAllocations(Long employeeId, Long projectId, LocalDate fromDate, LocalDate toDate) {
        return readMany(table -> table.ID.isNotNull());
    }

    public List<AllocationDto> getAllocationsByContractId(Long contractId) {
        return readMany(table -> table.CONTRACT_ID.eq(contractId));
    }

    public Optional<AllocationDto> getAllocationById(Long id) {
        return readOne(table -> table.ID.eq(id));
    }

    public Optional<AllocationDto> updateAllocation(Long id, AllocationDto allocation) {
        allocation.setId(id);
        return updateOne(allocation);
    }

    public void deleteAllocation(Long id) {
        deleteOne(table -> table.ID.eq(id));
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
