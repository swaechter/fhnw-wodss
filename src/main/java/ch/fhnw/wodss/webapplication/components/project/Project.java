package ch.fhnw.wodss.webapplication.components.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.util.Date;

@ApiModel(value = "Project", description = "Represents a FHNW research project with a given full-time-equivalent (FTE) workload in percentages managed by a project manager employee")
public class Project {

    @ApiModelProperty(value = "Project ID", allowableValues = "range[1, 9223372036854775807]", example = "42", position = 1)
    private Long id;

    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "Project name", allowableValues = "range[1, 50]", example = "IP5: Distributed IOT systems", position = 2)
    private String name;

    @ApiModelProperty(value = "Full time equivalent represented as a percentage value (1 FTE = 100% = 1 person working 1 day)", allowableValues = "range[1, 9223372036854775807]", example = "500 (= 5 FTE's)", position = 3)
    private Long ftePercentage;

    @ApiModelProperty(value = "Project start date (DD.MM.YYYY)", example = "13.03.2019", position = 4)
    private Date startDate;

    @ApiModelProperty(value = "Project end date (DD.MM.YYYY) - later or equal start date", example = "14.06.2019", position = 5)
    private Date endDate;

    @ApiModelProperty(value = "Project status", allowableValues = "true, false", example = "true", position = 6)
    private Boolean isActive;

    @ApiModelProperty(value = "Project manager employee ID", allowableValues = "range[1, 9223372036854775807]", example = "5", position = 7)
    private Long projectManagerId;

    public Project() {
    }

    public Project(String name, Long ftePercentage, Date startDate, Date endDate, Boolean isActive) {
        this.name = name;
        this.ftePercentage = ftePercentage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(Long projectManagerId) {
        this.projectManagerId = projectManagerId;
    }
}
