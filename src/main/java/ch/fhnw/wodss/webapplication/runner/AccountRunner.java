package ch.fhnw.wodss.webapplication.runner;

import ch.fhnw.wodss.webapplication.components.accounts.Account;
import ch.fhnw.wodss.webapplication.components.accounts.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class AccountRunner implements CommandLineRunner {

    private final AccountService accountService;

    public AccountRunner(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void run(String[] args) {
        accountService.createAccount("user", "password", Account.AccountType.USER);
        accountService.createAccount("admin", "password", Account.AccountType.ADIMN);
    }
}
