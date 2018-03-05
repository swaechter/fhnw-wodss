package ch.fhnw.wodss.webapplication.components.accounts;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class Account {

    public enum AccountType {
        USER,
        ADIMN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String userName;

    @NotNull
    @Column(nullable = false)
    private String passwordHash;

    @NotNull
    @Column(nullable = false)
    private AccountType accountType;

    // Mandatory JPA default constructor
    public Account() {
    }

    public Account(String userName, String passwordHash, AccountType accountType) {
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.accountType = accountType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
