package ch.fhnw.wodss.webapplication.components.contract;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContractService {

    public Contract createContract(Contract contract) {
        contract.setId(42L);
        return contract;
    }

    public List<Contract> getContracts(LocalDate fromDate, LocalDate toDate) {
        return new ArrayList<>();
    }

    public Contract getContract(Long id) {
        return new Contract();
    }

    public void updateContract(Long id, Contract contract) {
    }

    public void deleteContract(Long id) {
    }
}
