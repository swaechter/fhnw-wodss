package ch.fhnw.wodss.webapplication.components.accounts;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Account createAccount(String userName, String rawPassword, Account.AccountType accountType) {
        Account account = new Account(userName, passwordEncoder.encode(rawPassword), accountType);
        accountRepository.save(account);
        return account;
    }

    public Optional<Account> findByUserName(String userName) {
        return accountRepository.findByUserName(userName);
    }

    public Optional<Account> getAccount(String userName, String passwordHash) {
        return accountRepository.getAccountByUserNameAndPasswordHash(userName, passwordHash);
    }
}
