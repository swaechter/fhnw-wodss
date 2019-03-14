package ch.fhnw.wodss.webapplication.components.allocation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@ApiModel(value = "Allocation", description = "Represents the work unit an employee is doing for a project")
public class Allocation {

    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    @ApiModelProperty(value = "Allocation ID", allowableValues = "range[1, 9223372036854775807]", example = "42", readOnly = true, position = 1)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "Allocation start date (YYYY-MM-DD)", example = "2019-03-13", required = true, position = 2)
    private LocalDate startDate;

    @NotNull
    @ApiModelProperty(value = "Allocation end date (YYYY-MM-DD)", example = "2019-06-13", required = true, position = 3)
    private LocalDate endDate;

    @NotNull
    @Min(0)
    @Max(100)
    @ApiModelProperty(value = "Full time equivalent for the contract as percentage value (0.5 FTE = 50%)", allowableValues = "range[0, 100]", example = "50 (= 0.5 FTE)", required = true, position = 4)
    private Long pensumPercentage;

    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    @ApiModelProperty(value = "Employee ID of the allocation", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true, position = 5)
    private Long employeeId;

    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    @ApiModelProperty(value = "Project ID of the allocation", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true, position = 6)
    private Long projectId;

    public Allocation() {
    }

    public Allocation(LocalDate startDate, LocalDate endDate, Long pensumPercentage) {
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

    public Long getPensumPercentage() {
        return pensumPercentage;
    }

    public void setPensumPercentage(Long pensumPercentage) {
        this.pensumPercentage = pensumPercentage;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
