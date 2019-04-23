package ch.fhnw.wodss.webapplication.components.allocation;

import ch.fhnw.wodss.webapplication.components.contract.ContractDto;
import ch.fhnw.wodss.webapplication.components.contract.ContractRepository;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.employee.Role;
import ch.fhnw.wodss.webapplication.components.project.ProjectDto;
import ch.fhnw.wodss.webapplication.components.project.ProjectRepository;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InsufficientPermissionException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import ch.fhnw.wodss.webapplication.exceptions.InvalidActionException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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

    public AllocationDto createAllocation(AllocationDto allocation, EmployeeDto authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        Optional<ContractDto> selectedContract = contractRepository.getContractById(allocation.getContractId());
        if (selectedContract.isEmpty()) {
            throw new EntityNotFoundException("contract", allocation.getContractId());
        }

        Optional<ProjectDto> selectedProject = projectRepository.getProjectById(allocation.getProjectId());
        if (selectedProject.isEmpty()) {
            throw new EntityNotFoundException("project", allocation.getProjectId());
        }

        if (allocation.getStartDate().isBefore(selectedContract.get().getStartDate())) {
            throw new InvalidActionException("The start date of an allocation can't be before the contract start date");
        }

        if (allocation.getEndDate().isAfter(selectedContract.get().getEndDate())) {
            throw new InvalidActionException("The end date of an allocation can't be after the contract end date");
        }

        if (authenticatedEmployee.getRole() != Role.ADMINISTRATOR && !selectedProject.get().getProjectManagerId().equals(authenticatedEmployee.getId())) {
            throw new InsufficientPermissionException("Only an administrator or the assigned project manager of the project can create a new allocations");
        }

        List<AllocationDto> allocationDtos = allocationRepository.getAllocationsByContractId(selectedContract.get().getId());
        if (!canAllocationBeCreatedWithoutOverbooking(selectedContract.get(), allocationDtos, allocation)) {
            throw new InvalidActionException("Allocation can't be created because it would overbook the employee");
        }

        Optional<AllocationDto> createdAllocation = allocationRepository.saveAllocation(allocation);
        if (createdAllocation.isEmpty()) {
            throw new InternalException("Unable to create the allocation");
        }

        return createdAllocation.get();
    }

    public List<AllocationDto> getAllocations(UUID employeeId, UUID projectId, LocalDate fromDate, LocalDate toDate, EmployeeDto authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        // Simulate a more or less "matching-everything" filter - otherwise we have to deal with 4 different scenarios in the repository
        fromDate = fromDate != null ? fromDate : LocalDate.of(1900, 1, 1);
        toDate = toDate != null ? toDate : LocalDate.of(2100, 1, 1);

        if (!fromDate.isBefore(toDate)) {
            throw new InvalidActionException("The start date of the search filter has to be before the end date");
        }

        if (authenticatedEmployee.getRole() == Role.ADMINISTRATOR) {
            return allocationRepository.getAllocations(null, employeeId, projectId, fromDate, toDate);
        } else if (authenticatedEmployee.getRole() == Role.PROJECTMANAGER) {
            return allocationRepository.getAllocations(authenticatedEmployee.getId(), employeeId, projectId, fromDate, toDate);
        } else {
            return allocationRepository.getAllocations(null, authenticatedEmployee.getId(), projectId, fromDate, toDate);
        }
    }

    public AllocationDto getAllocation(UUID id, EmployeeDto authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        Optional<AllocationDto> selectedAllocation = allocationRepository.getAllocationById(id);
        if (selectedAllocation.isEmpty()) {
            throw new EntityNotFoundException("allocation", id);
        }

        Optional<ContractDto> selectedContract = contractRepository.getContractById(selectedAllocation.get().getContractId());
        if (selectedContract.isEmpty()) {
            throw new EntityNotFoundException("contract", selectedAllocation.get().getContractId());
        }

        Optional<ProjectDto> selectedProject = projectRepository.getProjectById(selectedAllocation.get().getProjectId());
        if (selectedProject.isEmpty()) {
            throw new EntityNotFoundException("project", selectedAllocation.get().getProjectId());
        }

        if (authenticatedEmployee.getRole() != Role.ADMINISTRATOR && !selectedProject.get().getProjectManagerId().equals(authenticatedEmployee.getId()) && !selectedContract.get().getEmployeeId().equals(authenticatedEmployee.getId())) {
            throw new InsufficientPermissionException("Only an administrator, the assigned project manager of the project or the employee of the allocation can access this allocation");
        }

        return selectedAllocation.get();
    }

    public AllocationDto updateAllocation(AllocationDto allocation, EmployeeDto authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        Optional<AllocationDto> selectedAllocation = allocationRepository.getAllocationById(allocation.getId());
        if (selectedAllocation.isEmpty()) {
            throw new EntityNotFoundException("allocation", allocation.getId());
        }

        Optional<ContractDto> selectedContract = contractRepository.getContractById(allocation.getContractId());
        if (selectedContract.isEmpty()) {
            throw new EntityNotFoundException("contract", allocation.getContractId());
        }

        Optional<ProjectDto> selectedProject = projectRepository.getProjectById(allocation.getProjectId());
        if (selectedProject.isEmpty()) {
            throw new EntityNotFoundException("project", allocation.getProjectId());
        }

        if (allocation.getStartDate().isBefore(selectedContract.get().getStartDate())) {
            throw new InvalidActionException("The start date of an allocation can't be before the contract start date");
        }

        if (allocation.getEndDate().isAfter(selectedContract.get().getEndDate())) {
            throw new InvalidActionException("The end date of an allocation can't be after the contract end date");
        }

        if (!allocation.getPensumPercentage().equals(selectedAllocation.get().getPensumPercentage())) {
            throw new InvalidActionException("The allocation pensum percentage can't be changed afterwards");
        }
        if (authenticatedEmployee.getRole() != Role.ADMINISTRATOR && !selectedProject.get().getProjectManagerId().equals(authenticatedEmployee.getId())) {
            throw new InsufficientPermissionException("Only an administrator or the assigned project manager of the project can update this allocation");
        }

        List<AllocationDto> allocationDtos = allocationRepository.getAllocationsByContractId(selectedContract.get().getId());
        if (!canAllocationBeCreatedWithoutOverbooking(selectedContract.get(), allocationDtos, allocation)) {
            throw new InvalidActionException("Allocation can't be created because it would overbook the employee");
        }

        Optional<AllocationDto> updatedAllocation = allocationRepository.updateAllocation(allocation);
        if (updatedAllocation.isEmpty()) {
            throw new InternalException("Unable to update the allocation");
        }

        return updatedAllocation.get();
    }

    public void deleteAllocation(UUID id, EmployeeDto authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        Optional<AllocationDto> selectedAllocation = allocationRepository.getAllocationById(id);
        if (selectedAllocation.isEmpty()) {
            throw new EntityNotFoundException("allocation", id);
        }

        Optional<ProjectDto> selectedProject = projectRepository.getProjectById(selectedAllocation.get().getProjectId());
        if (selectedProject.isEmpty()) {
            throw new EntityNotFoundException("project", selectedAllocation.get().getProjectId());
        }

        if (authenticatedEmployee.getRole() != Role.ADMINISTRATOR && !selectedProject.get().getProjectManagerId().equals(authenticatedEmployee.getId())) {
            throw new InsufficientPermissionException("Only an administrator or the assigned project manager of the project can delete this allocation");
        }

        allocationRepository.deleteAllocation(id);
    }

    private boolean canAllocationBeCreatedWithoutOverbooking(ContractDto contract, List<AllocationDto> existingAllocations, AllocationDto newAllocation) {
        // Get the working days and calculate the number of working days in percentages (250% = I have to work 2.5 days during the time period
        Integer contractWorkingDaysInPercentages = getWorkingDaysForDateRange(contract.getStartDate(), contract.getEndDate()) * contract.getPensumPercentage();

        // Substract all existing allocations
        for (AllocationDto allocationDto : existingAllocations) {
            // Get the working days of the contract and substract them
            Integer allocationWorkingDayInPercentages = getWorkingDaysForDateRange(allocationDto.getStartDate(), allocationDto.getEndDate()) * allocationDto.getPensumPercentage();
            contractWorkingDaysInPercentages -= allocationWorkingDayInPercentages;
        }

        if (contractWorkingDaysInPercentages < 0) {
            throw new IllegalStateException("The contract is already overbooked - this looks like an internal data error!");
        }

        // Get the working days percentage for the new allocation
        Integer allocationWorkingDayInPercentages = getWorkingDaysForDateRange(newAllocation.getStartDate(), newAllocation.getEndDate()) * newAllocation.getPensumPercentage();

        // Check the overbooking
        contractWorkingDaysInPercentages -= allocationWorkingDayInPercentages;
        return contractWorkingDaysInPercentages >= 0.0;
    }

    // Idea taken from: https://stackoverflow.com/questions/4600034/calculate-number-of-weekdays-between-two-dates-in-java/4600057
    // Changes: Don't make date check exclusive
    private Integer getWorkingDaysForDateRange(LocalDate startDate, LocalDate endDate) {
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(convertLocalDateToLocaleDate(startDate));

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(convertLocalDateToLocaleDate(endDate));

        int workingDays = 0;

        if (endCalendar.before(startCalendar)) {
            throw new IllegalStateException("The end date can't be before the start date");
        }

        if (startCalendar.equals(endCalendar)) {
            return 0;
        }

        while (!startCalendar.after(endCalendar)) {
            int day = startCalendar.get(Calendar.DAY_OF_WEEK);
            if (day != Calendar.SATURDAY && day != Calendar.SUNDAY) {
                workingDays++;
            }
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return workingDays;
    }

    private Date convertLocalDateToLocaleDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
