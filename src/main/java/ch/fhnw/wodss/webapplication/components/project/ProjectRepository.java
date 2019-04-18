package ch.fhnw.wodss.webapplication.components.project;

import ch.fhnw.wodss.webapplication.utils.Converter;
import ch.fhnw.wodss.webapplication.utils.GenericCrudRepository;
import org.jooq.DSLContext;
import org.jooq.generated.tables.Project;
import org.jooq.generated.tables.records.ProjectRecord;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ProjectRepository extends GenericCrudRepository<ProjectDto, ProjectRecord, Project> {

    public ProjectRepository(DSLContext dslContext, Converter converter) {
        super(dslContext, converter, Project.PROJECT);
    }

    public Optional<ProjectDto> saveProject(ProjectDto project) {
        project.setId(UUID.randomUUID());
        return createOne(project);
    }

    public List<ProjectDto> getProjects(LocalDate fromDate, LocalDate toDate, UUID projectManagerId) {
        return readMany(table -> table.ID.isNotNull());
    }

    public Optional<ProjectDto> getProjectById(UUID id) {
        return readOne(table -> table.ID.eq(id));
    }

    public Optional<ProjectDto> updateProject(ProjectDto project) {
        return updateOne(project);
    }

    public void deleteProject(UUID id) {
        deleteOne(table -> table.ID.eq(id));
    }

    @Override
    protected ProjectRecord mapDtoToRecord(ProjectDto project, ProjectRecord projectRecord) {
        if (project.getId() != null) {
            projectRecord.setId(project.getId());
        }
        projectRecord.setName(project.getName());
        projectRecord.setFtePercentage(project.getFtePercentage());
        projectRecord.setStartDate(getConverter().localDateToSqlDate(project.getStartDate()));
        projectRecord.setEndDate(getConverter().localDateToSqlDate(project.getEndDate()));
        projectRecord.setProjectManagerId(project.getProjectManagerId());
        return projectRecord;
    }

    @Override
    protected ProjectDto mapRecordToDto(ProjectRecord projectRecord) {
        return getConverter().projectRecordToProjectDto(projectRecord);
    }
}

