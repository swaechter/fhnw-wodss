package ch.fhnw.wodss.webapplication.components.accounts;

import ch.fhnw.wodss.webapplication.components.roles.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account createAccount(String userName, String rawPassword, Role role) {
        Account account = new Account(userName, passwordEncoder.encode(rawPassword), role);
        accountRepository.save(account);
        return account;
    }

    public Optional<Account> findByUserName(String userName) {
        return accountRepository.findByUserName(userName);
    }

    public Optional<Account> getAccount(String userName, String passwordHash) {
        return accountRepository.getAccountByUserNameAndPasswordHash(userName, passwordHash);
    }

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }
}
