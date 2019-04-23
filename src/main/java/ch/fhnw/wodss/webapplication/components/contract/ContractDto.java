package ch.fhnw.wodss.webapplication.components.contract;

import ch.fhnw.wodss.webapplication.utils.DateRange;
import ch.fhnw.wodss.webapplication.utils.ValidDateRange;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@JsonPropertyOrder({"id", "startDate", "endDate", "pensumPercentage", "employeeId"})
@ApiModel(value = "Contract", description = "Represents the contract an employee can have (Multiple contracts are possible, but date overlapping is not allowed)")
@ValidDateRange
public class ContractDto implements DateRange {

    @ApiModelProperty(value = "Contract ID", example = "010a7082-61b0-11e9-8647-d663bd873d93", readOnly = true, position = 1)
    private UUID id;

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
    private Short pensumPercentage;

    @NotNull
    @ApiModelProperty(value = "Employee ID of the contract", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true, position = 5)
    private UUID employeeId;

    public ContractDto() {
    }

    public ContractDto(LocalDate startDate, LocalDate endDate, Short pensumPercentage) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.pensumPercentage = pensumPercentage;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public Short getPensumPercentage() {
        return pensumPercentage;
    }

    public void setPensumPercentage(Short pensumPercentage) {
        this.pensumPercentage = pensumPercentage;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }
}
