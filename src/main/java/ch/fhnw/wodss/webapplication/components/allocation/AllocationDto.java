package ch.fhnw.wodss.webapplication.components.allocation;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@JsonPropertyOrder({"id", "startDate", "endDate", "pensumPercentage", "contractId", "projectId"})
@ApiModel(value = "Allocation", description = "Represents the work unit an employee is doing for a project")
public class AllocationDto {

    @ApiModelProperty(value = "Allocation ID", example = "010a7082-61b0-11e9-8647-d663bd873d93", readOnly = true, position = 1)
    private UUID id;

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
    @ApiModelProperty(value = "Contract ID of the allocation", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true, position = 5)
    private UUID contractId;

    @NotNull
    @ApiModelProperty(value = "Project ID of the allocation", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true, position = 6)
    private UUID projectId;

    public AllocationDto() {
    }

    public AllocationDto(UUID id, LocalDate startDate, LocalDate endDate, Short pensumPercentage, UUID contractId, UUID projectId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pensumPercentage = pensumPercentage;
        this.contractId = contractId;
        this.projectId = projectId;
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

    public UUID getContractId() {
        return contractId;
    }

    public void setContractId(UUID contractId) {
        this.contractId = contractId;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }
}
