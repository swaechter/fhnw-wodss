package ch.fhnw.wodss.webapplication.components.contract;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public ContractDto createContract(ContractDto contract) {
        return contractRepository.saveEntry(contract);
    }

    public List<ContractDto> getContracts(LocalDate fromDate, LocalDate toDate) {
        return contractRepository.getEntries();
    }

    public ContractDto getContract(Long id) {
        return contractRepository.getEntry(id);
    }

    public void updateContract(Long id, ContractDto contract) {
        contractRepository.updateEntry(id, contract);
    }

    public void deleteContract(Long id) {
        contractRepository.deleteEntry(id);
    }
}
