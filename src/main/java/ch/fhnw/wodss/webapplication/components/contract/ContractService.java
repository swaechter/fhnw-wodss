package ch.fhnw.wodss.webapplication.components.contract;

import ch.fhnw.wodss.webapplication.components.allocation.AllocationDto;
import ch.fhnw.wodss.webapplication.components.allocation.AllocationRepository;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeRepository;
import ch.fhnw.wodss.webapplication.components.employee.Role;
import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InsufficientPermissionException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import ch.fhnw.wodss.webapplication.exceptions.InvalidActionException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    private AllocationRepository allocationRepository;

    private final EmployeeRepository employeeRepository;

    public ContractService(ContractRepository contractRepository, AllocationRepository allocationRepository, EmployeeRepository employeeRepository) {
        this.contractRepository = contractRepository;
        this.allocationRepository = allocationRepository;
        this.employeeRepository = employeeRepository;
    }

    public ContractDto createContract(ContractDto contract, AuthenticatedEmployee authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        if (authenticatedEmployee.getRole() != Role.ADMINISTRATOR || authenticatedEmployee.getRole() != Role.PROJECTMANAGER) {
            throw new InsufficientPermissionException("Only administrators and project managers can create new contracts");
        }

        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(contract.getEmployeeId());
        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", contract.getEmployeeId());
        }

        if (!contract.getStartDate().isBefore(contract.getEndDate())) {
            throw new InvalidActionException("The start date of a contract has to be before the end date");
        }

        List<ContractDto> existingContractDtos = contractRepository.getContracts(contract.getStartDate(), contract.getEndDate(), contract.getEmployeeId());
        if (!existingContractDtos.isEmpty()) {
            throw new InvalidActionException("There already exists an overlapping contract for this time period");
        }

        Optional<ContractDto> createdContract = contractRepository.saveContract(contract);
        if (createdContract.isEmpty()) {
            throw new InternalException("Unable to create the contract");
        }

        return createdContract.get();
    }

    public List<ContractDto> getContracts(LocalDate fromDate, LocalDate toDate, AuthenticatedEmployee authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        boolean showAllContracts = authenticatedEmployee.getRole() == Role.ADMINISTRATOR || authenticatedEmployee.getRole() == Role.PROJECTMANAGER;
        return contractRepository.getContracts(fromDate, toDate, showAllContracts ? null : authenticatedEmployee.getId());
    }

    public ContractDto getContract(Long id, AuthenticatedEmployee authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        Optional<ContractDto> selectedContract = contractRepository.getContractById(id);
        if (selectedContract.isEmpty()) {
            throw new EntityNotFoundException("contract", id);
        }

        if ((authenticatedEmployee.getRole() != Role.ADMINISTRATOR && authenticatedEmployee.getRole() != Role.PROJECTMANAGER) || !authenticatedEmployee.getId().equals(selectedContract.get().getEmployeeId())) {
            throw new InsufficientPermissionException("Only administrators, project managers or the employee itself can access this contract");
        }

        return selectedContract.get();
    }

    public ContractDto updateContract(Long id, ContractDto contract, AuthenticatedEmployee authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        if (authenticatedEmployee.getRole() != Role.ADMINISTRATOR) {
            throw new InsufficientPermissionException("Only administrators can update a contract");
        }

        Optional<ContractDto> selectedContract = contractRepository.getContractById(id);
        if (selectedContract.isEmpty()) {
            throw new EntityNotFoundException("contract", id);
        }

        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(contract.getEmployeeId());
        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", contract.getEmployeeId());
        }

        if (!contract.getStartDate().isBefore(contract.getEndDate())) {
            throw new InvalidActionException("The start date of a contract has to be before the end date");
        }

        List<ContractDto> existingContractDtos = contractRepository.getContracts(contract.getStartDate(), contract.getEndDate(), contract.getEmployeeId());
        if (!existingContractDtos.isEmpty() && !existingContractDtos.get(0).getId().equals(contract.getId())) {
            throw new InvalidActionException("There already exists an overlapping contract for this time period");
        }

        Optional<ContractDto> updatedContract = contractRepository.updateContract(id, contract);
        if (updatedContract.isEmpty()) {
            throw new InternalException("Unable to update the contract");
        }

        return updatedContract.get();
    }

    public void deleteContract(Long id, AuthenticatedEmployee authenticatedEmployee) {
        if (!authenticatedEmployee.isActive()) {
            throw new InvalidActionException("The authenticated employee is not activated");
        }

        if (authenticatedEmployee.getRole() != Role.ADMINISTRATOR) {
            throw new InsufficientPermissionException("Only administrators can delete a contract");
        }

        Optional<ContractDto> selectedContract = contractRepository.getContractById(id);
        if (selectedContract.isEmpty()) {
            throw new EntityNotFoundException("contract", id);
        }

        List<AllocationDto> referencedAllocations = allocationRepository.getAllocationsByContractId(id);
        if (!referencedAllocations.isEmpty()) {
            throw new InvalidActionException("The contract can't be deleted because it is already referenced by allocations (Delete them before)");
        }

        contractRepository.deleteContract(id);
    }
}
