package ch.fhnw.wodss.webapplication.components.contract;

import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeRepository;
import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    private final EmployeeRepository employeeRepository;

    public ContractService(ContractRepository contractRepository, EmployeeRepository employeeRepository) {
        this.contractRepository = contractRepository;
        this.employeeRepository = employeeRepository;
    }

    public ContractDto createContract(ContractDto contract, AuthenticatedEmployee authenticatedEmployee) {
        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(contract.getEmployeeId());
        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", contract.getEmployeeId());
        }

        Optional<ContractDto> createdContract = contractRepository.saveContract(contract);
        if (createdContract.isEmpty()) {
            throw new InternalException("Unable to create the contract");
        }

        return createdContract.get();
    }

    public List<ContractDto> getContracts(LocalDate fromDate, LocalDate toDate, AuthenticatedEmployee authenticatedEmployee) {
        return contractRepository.getContracts(fromDate, toDate);
    }

    public ContractDto getContract(Long id, AuthenticatedEmployee authenticatedEmployee) {
        Optional<ContractDto> selectedContract = contractRepository.getContractById(id);
        if (selectedContract.isEmpty()) {
            throw new EntityNotFoundException("contract", id);
        }

        return selectedContract.get();
    }

    public ContractDto updateContract(Long id, ContractDto contract, AuthenticatedEmployee authenticatedEmployee) {
        Optional<ContractDto> selectedContract = contractRepository.getContractById(id);
        if (selectedContract.isEmpty()) {
            throw new EntityNotFoundException("contract", id);
        }

        Optional<EmployeeDto> selectedEmployee = employeeRepository.getEmployeeById(contract.getEmployeeId());
        if (selectedEmployee.isEmpty()) {
            throw new EntityNotFoundException("employee", contract.getEmployeeId());
        }

        Optional<ContractDto> updatedContract = contractRepository.updateContract(id, contract);
        if (updatedContract.isEmpty()) {
            throw new InternalException("Unable to update the contract");
        }

        return updatedContract.get();
    }

    public void deleteContract(Long id, AuthenticatedEmployee authenticatedEmployee) {
        Optional<ContractDto> selectedContract = contractRepository.getContractById(id);
        if (selectedContract.isEmpty()) {
            throw new EntityNotFoundException("contract", id);
        }

        contractRepository.deleteContract(id);
    }
}
