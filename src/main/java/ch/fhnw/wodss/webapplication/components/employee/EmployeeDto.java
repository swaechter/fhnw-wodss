package ch.fhnw.wodss.webapplication.components.employee;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;

@JsonPropertyOrder({"id", "firstName", "lastName", "emailAddress", "isActive", "role"})
@ApiModel(value = "Employee", description = "Represents the employee of the FHNW. An employee can have several non-overlapping contracts. In addition he can work in multiple projects and act as project leader")
public class EmployeeDto {

    @ApiModelProperty(value = "Employee ID", allowableValues = "range[1, 9223372036854775807]", example = "42", readOnly = true, position = 1)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "Employee first name", allowableValues = "range[1, 50]", example = "Simon", required = true, position = 2)
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "Employee last name", allowableValues = "range[1, 50]", example = "Wächter", required = true, position = 3)
    private String lastName;

    @NotBlank
    @Size(min = 1, max = 120)
    @ApiModelProperty(value = "Employee email address", allowableValues = "range[1, 120]", example = "simon.waechter@students.fhnw.ch", required = true, position = 4)
    private String emailAddress;

    @NotNull
    @ApiModelProperty(value = "Employee registration status (Enabled by admin or not)", allowableValues = "true, false", example = "true", required = true, position = 5)
    private Boolean isActive;

    @ApiModelProperty(value = "Single employee role", allowableValues = "ADMINISTRATOR, PROJECTMANAGER, DEVELOPER", example = "DEVELOPER", readOnly = true, position = 6)
    private Role role;

    // TODO: Kill this temporary password hash field, because we can't rely on the database at the moment + the employee DTO shouldn't have a password hash field
    @JsonIgnore
    private String temporaryPasswordHash;

    public EmployeeDto() {
    }

    public EmployeeDto(String firstName, String lastName, String emailAddress, Boolean isActive, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.isActive = isActive;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    // TODO: Delete this method as soon we rely on the database
    public String getTemporaryPasswordHash() {
        return temporaryPasswordHash;
    }

    // TODO: Delete this method as soon we rely on the database
    public void setTemporaryPasswordHash(String temporaryPasswordHash) {
        this.temporaryPasswordHash = temporaryPasswordHash;
    }
}
