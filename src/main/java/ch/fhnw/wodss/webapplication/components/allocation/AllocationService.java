package ch.fhnw.wodss.webapplication.components.allocation;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AllocationService {

    private final AllocationRepository allocationRepository;

    public AllocationService(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    public Allocation createAllocation(Allocation allocation) {
        return allocationRepository.saveEntry(allocation);
    }

    public List<Allocation> getAllocations(Long employeeId, Long projectId, LocalDate fromDate, LocalDate toDate) {
        return allocationRepository.getEntries();
    }

    public Allocation getAllocation(Long id) {
        return allocationRepository.getEntry(id);
    }

    public void updateAllocation(Long id, Allocation allocation) {
        allocationRepository.updateEntry(id, allocation);
    }

    public void deleteAllocation(Long id) {
        allocationRepository.deleteEntry(id);
    }
}
