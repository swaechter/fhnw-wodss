package ch.fhnw.wodss.webapplication.components.allocation;

import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AllocationService {

    private final AllocationRepository allocationRepository;

    public AllocationService(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    public AllocationDto createAllocation(AllocationDto allocation, AuthenticatedEmployee authenticatedEmployee) {
        return allocationRepository.saveEntry(allocation);
    }

    public List<AllocationDto> getAllocations(Long employeeId, Long projectId, LocalDate fromDate, LocalDate toDate, AuthenticatedEmployee authenticatedEmployee) {
        return allocationRepository.getEntries();
    }

    public AllocationDto getAllocation(Long id, AuthenticatedEmployee authenticatedEmployee) {
        return allocationRepository.getEntry(id);
    }

    public void updateAllocation(Long id, AllocationDto allocation, AuthenticatedEmployee authenticatedEmployee) {
        allocationRepository.updateEntry(id, allocation);
    }

    public void deleteAllocation(Long id, AuthenticatedEmployee authenticatedEmployee) {
        allocationRepository.deleteEntry(id);
    }
}
