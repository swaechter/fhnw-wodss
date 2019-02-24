package ch.fhnw.wodss.webapplication.components.accounts;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@Api(tags = "Accounts", description = "Endpoint for managing all accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping()
    @ApiOperation("Get all user accounts")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "All user accounts")
    })
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }
}
