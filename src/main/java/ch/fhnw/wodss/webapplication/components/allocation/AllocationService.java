package ch.fhnw.wodss.webapplication.components.allocation;

import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AllocationService {

    private final AllocationRepository allocationRepository;

    public AllocationService(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    public AllocationDto createAllocation(AllocationDto allocation, AuthenticatedEmployee authenticatedEmployee) {
        Optional<AllocationDto> createdAllocation = allocationRepository.saveAllocation(allocation);
        if (createdAllocation.isEmpty()) {
            throw new InternalException("Unable to create the allocation");
        }

        return createdAllocation.get();
    }

    public List<AllocationDto> getAllocations(Long employeeId, Long projectId, LocalDate fromDate, LocalDate toDate, AuthenticatedEmployee authenticatedEmployee) {
        return allocationRepository.getAllocations(employeeId, projectId, fromDate, toDate);
    }

    public AllocationDto getAllocation(Long id, AuthenticatedEmployee authenticatedEmployee) {
        Optional<AllocationDto> selectedAllocation = allocationRepository.getAllocationById(id);
        if (selectedAllocation.isEmpty()) {
            throw new EntityNotFoundException("allocation", id);
        }

        return selectedAllocation.get();
    }

    public AllocationDto updateAllocation(Long id, AllocationDto allocation, AuthenticatedEmployee authenticatedEmployee) {
        Optional<AllocationDto> updatedAllocation = allocationRepository.updateAllocation(id, allocation);
        if (updatedAllocation.isEmpty()) {
            throw new EntityNotFoundException("allocation", id);
        }

        return updatedAllocation.get();
    }

    public void deleteAllocation(Long id, AuthenticatedEmployee authenticatedEmployee) {
        allocationRepository.deleteAllocation(id);
    }
}
