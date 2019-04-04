package ch.fhnw.wodss.webapplication.components.contract;

import ch.fhnw.wodss.webapplication.utils.MemoryRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ContractRepository extends MemoryRepository<ContractDto> {

    private final DSLContext dslContext;

    public ContractRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<ContractDto> getContracts() {
        return new ArrayList<>();
    }

    @Override
    public void setEntityId(ContractDto entry, Long value) {
        entry.setId(value);
    }
}
