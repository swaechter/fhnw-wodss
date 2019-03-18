package ch.fhnw.wodss.webapplication.components.token;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@JsonPropertyOrder({"token"})
@ApiModel(value = "Token", description = "Represents a login token with an employee claim that contains the whole employee as JSON object")
public class Token {

    @NotBlank
    @ApiModelProperty(value = "Login token with the employee claim that contains the full employee as JSON object (You can use a tool like http://calebb.net/ to inspect tokens)", example = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJGSE5XIHdvZHNzIiwic3ViIjoiTG9naW4gdG9rZW4iLCJlbXBsb3llZSI6eyJpZCI6NDIsImZpcnN0TmFtZSI6IlNpbW9uIiwibGFzdE5hbWUiOiJXw6RjaHRlciIsImVtYWlsQWRkcmVzcyI6InNpbW9uLndhZWNodGVyQHN0dWRlbnRzLmZobncuY2giLCJyb2xlIjoiQURNSU5JU1RSQVRPUiIsImFjdGl2ZSI6dHJ1ZX0sImlhdCI6MTU1Mjg4OTMxMSwiZXhwIjoxNTUyODkxMTExfQ.illaIr06f9ujL1VH857cjl5uPzqLTLdg1Em40wpCopw4pcacgUxzjxL_lx5zlK504UtpEGVReIPoelU1G3atZQ", required = true, position = 1)
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
