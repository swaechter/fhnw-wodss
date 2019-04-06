package ch.fhnw.wodss.webapplication.components.allocation;

import ch.fhnw.wodss.webapplication.components.contract.ContractDto;
import ch.fhnw.wodss.webapplication.components.contract.ContractRepository;
import ch.fhnw.wodss.webapplication.components.project.ProjectDto;
import ch.fhnw.wodss.webapplication.components.project.ProjectRepository;
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

    private final ContractRepository contractRepository;

    private final ProjectRepository projectRepository;

    public AllocationService(AllocationRepository allocationRepository, ContractRepository contractRepository, ProjectRepository projectRepository) {
        this.allocationRepository = allocationRepository;
        this.contractRepository = contractRepository;
        this.projectRepository = projectRepository;
    }

    public AllocationDto createAllocation(AllocationDto allocation, AuthenticatedEmployee authenticatedEmployee) {
        Optional<ContractDto> selectedContract = contractRepository.getContractById(allocation.getContractId());
        if (selectedContract.isEmpty()) {
            throw new EntityNotFoundException("contract", allocation.getContractId());
        }

        Optional<ProjectDto> selectedProject = projectRepository.getProjectById(allocation.getProjectId());
        if (selectedProject.isEmpty()) {
            throw new EntityNotFoundException("project", allocation.getProjectId());
        }

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
        Optional<AllocationDto> selectedAllocation = allocationRepository.getAllocationById(id);
        if (selectedAllocation.isEmpty()) {
            throw new EntityNotFoundException("allocation", id);
        }

        Optional<ContractDto> selectedContract = contractRepository.getContractById(allocation.getContractId());
        if (selectedContract.isEmpty()) {
            throw new EntityNotFoundException("contract", allocation.getContractId());
        }

        Optional<ProjectDto> selectedProject = projectRepository.getProjectById(allocation.getProjectId());
        if (selectedProject.isEmpty()) {
            throw new EntityNotFoundException("project", allocation.getProjectId());
        }

        Optional<AllocationDto> updatedAllocation = allocationRepository.updateAllocation(id, allocation);
        if (updatedAllocation.isEmpty()) {
            throw new InternalException("Unable to update the allocation");
        }

        return updatedAllocation.get();
    }

    public void deleteAllocation(Long id, AuthenticatedEmployee authenticatedEmployee) {
        Optional<AllocationDto> selectedAllocation = allocationRepository.getAllocationById(id);
        if (selectedAllocation.isEmpty()) {
            throw new EntityNotFoundException("allocation", id);
        }

        allocationRepository.deleteAllocation(id);
    }
}
