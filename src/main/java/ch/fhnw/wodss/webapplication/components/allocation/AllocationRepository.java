package ch.fhnw.wodss.webapplication.components.allocation;

import ch.fhnw.wodss.webapplication.utils.MemoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AllocationRepository extends MemoryRepository<AllocationDto> {

    @Override
    public void setEntityId(AllocationDto entry, Long value) {
        entry.setId(value);
    }
}
