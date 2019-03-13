package ch.fhnw.wodss.webapplication.components.allocation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "Allocation", description = "Represents the work unit an employee is doing for a project")
public class Allocation {

    @ApiModelProperty(value = "Allocation ID", allowableValues = "range[1, 9223372036854775807]", example = "42", position = 1)
    private Long id;

    @ApiModelProperty(value = "Allocation start date (DD.MM.YYYY)", example = "13.03.2019", position = 2)
    private Date startDate;

    @ApiModelProperty(value = "Allocation end date (DD.MM.YYYY) - later or equal start date", example = "14.06.2019", position = 3)
    private Date endDate;

    @ApiModelProperty(value = "Full time equivalent for the contract as percentage value (0.5 FTE = 50%)", allowableValues = "range[0, 100]", example = "50 (= 0.5 FTE)", position = 4)
    private Long pensumPercentage;

    @ApiModelProperty(value = "Employee ID of the allocation", allowableValues = "range[1, 9223372036854775807]", example = "42", position = 5)
    private Long employeeId;

    @ApiModelProperty(value = "Project ID of the allocation", allowableValues = "range[1, 9223372036854775807]", example = "42", position = 6)
    private Long projectId;

    public Allocation() {
    }

    public Allocation(Date startDate, Date endDate, Long pensumPercentage) {
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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
