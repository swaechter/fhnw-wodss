package ch.fhnw.wodss.webapplication.components.accounts;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    private final AccountService accountService;

    public AccountUserDetailsService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Account> account = accountService.findByUserName(userName);
        if (!account.isPresent()) {
            throw new UsernameNotFoundException(userName);
        }

        // NOTE: An authority without a ROLE_ prefix is an authority, one with the prefix a Role
        // For more information see: http://www.baeldung.com/spring-security-granted-authority-vs-role
        Account realAccount = account.get();
        ArrayList<GrantedAuthority> authority = new ArrayList<>();
        authority.add(new SimpleGrantedAuthority(realAccount.getRole().getName()));
        return new User(realAccount.getUserName(), realAccount.getPasswordHash(), authority);
    }
}
