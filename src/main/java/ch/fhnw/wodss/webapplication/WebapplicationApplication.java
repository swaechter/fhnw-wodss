package ch.fhnw.wodss.webapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebapplicationApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebapplicationApplication.class, args);
    }

/*    @Service
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
    }*/
}
