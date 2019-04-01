package ch.fhnw.wodss.webapplication.components.contract;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@JsonPropertyOrder({"id", "startDate", "endDate", "pensumPercentage", "employeeId"})
@ApiModel(value = "Contract", description = "Represents the contract an employee can have (Multiple contracts are possible, but date overlapping is not allowed)")
public class ContractDto {

    @ApiModelProperty(value = "Contract ID", allowableValues = "range[1, 9223372036854775807]", example = "42", readOnly = true, position = 1)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "Contract start date (YYYY-MM-DD)", example = "2019-03-13", required = true, position = 2)
    private LocalDate startDate;

    @NotNull
    @ApiModelProperty(value = "Contract end date (YYYY-MM-DD)", example = "2019-06-13", required = true, position = 3)
    private LocalDate endDate;

    @NotNull
    @Min(0)
    @Max(100)
    @ApiModelProperty(value = "Full time equivalent for the contract as percentage value (0.5 FTE = 50)", allowableValues = "range[0, 100]", example = "50", required = true, position = 4)
    private Integer pensumPercentage;

    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    @ApiModelProperty(value = "Employee ID of the contract", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true, position = 5)
    private Long employeeId;

    public ContractDto() {
    }

    public ContractDto(LocalDate startDate, LocalDate endDate, Integer pensumPercentage) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.pensumPercentage = pensumPercentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getPensumPercentage() {
        return pensumPercentage;
    }

    public void setPensumPercentage(Integer pensumPercentage) {
        this.pensumPercentage = pensumPercentage;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
