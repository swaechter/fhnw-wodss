package ch.fhnw.wodss.webapplication.components.token;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@JsonPropertyOrder({"token"})
@ApiModel(value = "Token", description = "Represents a login token with an employee claim that contains the whole employee as JSON object")
public class Token {

    @NotBlank
    @ApiModelProperty(value = "Login token with the employee claim that contains the full employee as JSON object (You can use a tool like http://calebb.net/ to inspect tokens)", required = true, position = 1)
    private String token;

    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
