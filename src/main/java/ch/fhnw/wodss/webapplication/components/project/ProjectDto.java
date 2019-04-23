package ch.fhnw.wodss.webapplication.components.project;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.UUID;

@JsonPropertyOrder({"id", "name", "ftePercentage", "startDate", "endDate", "projectManagerId"})
@ApiModel(value = "Project", description = "Represents a FHNW research project with a given full-time-equivalent (FTE) workload in percentages managed by a project manager employee")
public class ProjectDto {

    @ApiModelProperty(value = "Project ID", example = "010a7082-61b0-11e9-8647-d663bd873d93", readOnly = true, position = 1)
    private UUID id;

    @NotBlank
    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "Project name", allowableValues = "range[1, 50]", example = "IP5: Distributed IOT systems", required = true, position = 2)
    private String name;

    @NotNull
    @Min(0)
    @Max(Long.MAX_VALUE)
    @ApiModelProperty(value = "Full time equivalent represented as a percentage value (1 FTE = 100% = 1 person working 1 day)", allowableValues = "range[0, 9223372036854775807]", example = "1500", required = true, position = 3)
    private Long ftePercentage;

    @NotNull
    @ApiModelProperty(value = "Project start date (YYYY-MM-DD)", example = "2019-03-13", required = true, position = 4)
    private LocalDate startDate;

    @NotNull
    @ApiModelProperty(value = "Project end date (YYYY-MM-DD)", example = "2019-06-13", required = true, position = 5)
    private LocalDate endDate;

    @NotNull
    @ApiModelProperty(value = "Project manager employee ID", example = "010a7082-61b0-11e9-8647-d663bd873d93", required = true, position = 7)
    private UUID projectManagerId;

    public ProjectDto() {
    }

    public ProjectDto(ProjectDto p) {
        this.id = p.id;
        this.projectManagerId = p.projectManagerId;
        this.name = p.name;
        this.ftePercentage = p.ftePercentage;
        this.startDate = p.startDate;
        this.endDate = p.endDate;
    }

    public ProjectDto(String name, Long ftePercentage, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.ftePercentage = ftePercentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFtePercentage() {
        return ftePercentage;
    }

    public void setFtePercentage(Long ftePercentage) {
        this.ftePercentage = ftePercentage;
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

    public UUID getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(UUID projectManagerId) {
        this.projectManagerId = projectManagerId;
    }
}
