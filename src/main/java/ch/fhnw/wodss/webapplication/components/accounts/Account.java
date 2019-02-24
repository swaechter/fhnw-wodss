package ch.fhnw.wodss.webapplication.components.accounts;

import ch.fhnw.wodss.webapplication.components.roles.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @Column(name = "ACCOUNT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "USERNAME", nullable = false)
    private String userName;

    @NotNull
    @Column(name = "PASSWORD_HASH", nullable = false)
    @JsonIgnore // Don't show the password hash to the client
    private String passwordHash;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ROLE_ID") // TODO: Use a DTO with the role ID to break up the entity relationship
    private Role role; // TODO: Use a better one-to-many relationship + JPQL (Minimized queries)

    // Mandatory JPA default constructor
    public Account() {
    }

    public Account(String userName, String passwordHash, Role role) {
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
