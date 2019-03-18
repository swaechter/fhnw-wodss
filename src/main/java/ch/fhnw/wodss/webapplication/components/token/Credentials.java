package ch.fhnw.wodss.webapplication.components.token;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@JsonPropertyOrder({"emailAddress", "rawPassword"})
@ApiModel(value = "Credentials", description = "Represents the credentials of employee with an email address and a raw password (Based on these information a JWT token is then issued)")
public class Credentials {

    @NotBlank
    @Size(min = 1, max = 120)
    @ApiModelProperty(value = "Employee email address", allowableValues = "range[1, 120]", example = "simon.waechter@students.fhnw.ch", required = true, position = 1)
    private String emailAddress;

    @NotBlank
    @ApiModelProperty(value = "Raw employee password", example = "123456aA", required = true, position = 2)
    private String rawPassword;

    public Credentials() {
    }

    public Credentials(String emailAddress, String rawPassword) {
        this.emailAddress = emailAddress;
        this.rawPassword = rawPassword;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }
}
