package ch.fhnw.wodss.webapplication.components.allocation;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AllocationService {

    public Allocation createAllocation(Allocation allocation) {
        allocation.setId(42L);
        return allocation;
    }

    public List<Allocation> getAllocations(Long employeeId, Long contractId, Long projectId) {
        return new ArrayList<>();
    }

    public Allocation getAllocation(Long id) {
        return new Allocation();
    }

    public void updateAllocation(Long id, Allocation allocation) {
    }

    public void deleteAllocation(Long id) {
    }
}
