package ch.fhnw.wodss.webapplication.components.contract;

import ch.fhnw.wodss.webapplication.utils.MemoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ContractRepository extends MemoryRepository<ContractDto> {

    @Override
    public void setEntityId(ContractDto entry, Long value) {
        entry.setId(value);
    }
}
