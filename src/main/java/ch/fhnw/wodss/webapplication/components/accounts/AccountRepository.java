package ch.fhnw.wodss.webapplication.components.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserName(String userName);

    Optional<Account> getAccountByUserNameAndPasswordHash(String userName, String passwordHash);
}
