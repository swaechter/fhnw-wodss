package ch.fhnw.wodss.webapplication.components.contract;

import ch.fhnw.wodss.webapplication.configuration.AuthenticatedEmployee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public ContractDto createContract(ContractDto contract, AuthenticatedEmployee authenticatedEmployee) {
        return contractRepository.saveEntry(contract);
    }

    public List<ContractDto> getContracts(LocalDate fromDate, LocalDate toDate, AuthenticatedEmployee authenticatedEmployee) {
        return contractRepository.getEntries();
    }

    public ContractDto getContract(Long id, AuthenticatedEmployee authenticatedEmployee) {
        return contractRepository.getEntry(id);
    }

    public void updateContract(Long id, ContractDto contract, AuthenticatedEmployee authenticatedEmployee) {
        contractRepository.updateEntry(id, contract);
    }

    public void deleteContract(Long id, AuthenticatedEmployee authenticatedEmployee) {
        contractRepository.deleteEntry(id);
    }
}
