package ch.fhnw.wodss.webapplication.components.employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@JsonPropertyOrder({"id", "firstName", "lastName", "emailAddress", "isActive", "role"})
@ApiModel(value = "Employee", description = "Represents the employee of the FHNW. An employee can have several non-overlapping contracts. In addition he can work in multiple projects and act as project leader")
public class EmployeeDto {

    @ApiModelProperty(value = "Employee ID", example = "010a7082-61b0-11e9-8647-d663bd873d93", readOnly = true, position = 1)
    private UUID id;

    @NotBlank
    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "Employee first name", allowableValues = "range[1, 50]", example = "Simon", required = true, position = 2)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "Employee last name", allowableValues = "range[1, 50]", example = "Wächter", required = true, position = 3)
    private String lastName;

    @NotBlank
    @Email
    @Size(min = 1, max = 120)
    @ApiModelProperty(value = "Employee email address", allowableValues = "range[1, 120]", example = "simon.waechter@students.fhnw.ch", required = true, position = 4)
    private String emailAddress;

    @NotNull
    @ApiModelProperty(value = "Employee registration status (Enabled by admin or not)", allowableValues = "true, false", example = "true", required = true, position = 5)
    private Boolean isActive;

    @ApiModelProperty(value = "Single employee role", allowableValues = "ADMINISTRATOR, PROJECTMANAGER, DEVELOPER", example = "DEVELOPER", readOnly = true, position = 6)
    private Role role;

    @JsonIgnore
    private String passwordHash;

    public EmployeeDto() {
    }

    public EmployeeDto(EmployeeDto employee) {
        this.id = employee.id;
        this.firstName = employee.firstName;
        this.lastName = employee.lastName;
        this.emailAddress = employee.emailAddress;
        this.isActive = employee.isActive;
        this.role = employee.role;
        this.passwordHash = employee.passwordHash;
    }

    public EmployeeDto(UUID id, String firstName, String lastName, String emailAddress, String passwordHash, Boolean isActive, Role role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.passwordHash = passwordHash;
        this.isActive = isActive;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
