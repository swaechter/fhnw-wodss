package ch.fhnw.wodss.webapplication;

import ch.fhnw.wodss.webapplication.components.accounts.AccountService;
import ch.fhnw.wodss.webapplication.components.roles.Role;
import ch.fhnw.wodss.webapplication.components.roles.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

@SpringBootApplication
public class WebapplicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebapplicationApplication.class, args);
    }

    @Service
    public class AccountRunner implements CommandLineRunner {

        private final RoleService roleService;

        private final AccountService accountService;

        public AccountRunner(RoleService roleService, AccountService accountService) {
            this.roleService = roleService;
            this.accountService = accountService;
        }

        @Override
        public void run(String[] args) {
            Role userRole = roleService.createRole("ROLE_USER");
            Role adminRole = roleService.createRole("ROLE_ADMIN");

            accountService.createAccount("user", "user", userRole);
            accountService.createAccount("admin", "admin", adminRole);
        }
    }

}
