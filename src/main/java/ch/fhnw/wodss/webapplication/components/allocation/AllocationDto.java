package ch.fhnw.wodss.webapplication.components.allocation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@JsonPropertyOrder({"id", "startDate", "endDate", "pensumPercentage", "contractId", "projectId"})
@ApiModel(value = "Allocation", description = "Represents the work unit an employee is doing for a project")
public class AllocationDto {

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
    @ApiModelProperty(value = "Full time equivalent for the contract as percentage value (0.5 FTE = 50)", allowableValues = "range[0, 100]", example = "50", required = true, position = 4)
    private Short pensumPercentage;

    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    @ApiModelProperty(value = "Contract ID of the allocation", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true, position = 5)
    private Long contractId;

    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    @ApiModelProperty(value = "Project ID of the allocation", allowableValues = "range[1, 9223372036854775807]", example = "42", required = true, position = 6)
    private Long projectId;

    public AllocationDto() {
    }

    public AllocationDto(Long id, LocalDate startDate, LocalDate endDate, Short pensumPercentage, Long contractId, Long projectId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pensumPercentage = pensumPercentage;
        this.contractId = contractId;
        this.projectId = projectId;
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

    public Short getPensumPercentage() {
        return pensumPercentage;
    }

    public void setPensumPercentage(Short pensumPercentage) {
        this.pensumPercentage = pensumPercentage;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
