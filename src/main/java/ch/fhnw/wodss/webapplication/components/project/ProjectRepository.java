package ch.fhnw.wodss.webapplication.components.project;

import ch.fhnw.wodss.webapplication.utils.Converter;
import ch.fhnw.wodss.webapplication.utils.GenericCrudRepository;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectSeekStep1;
import org.jooq.generated.tables.Allocation;
import org.jooq.generated.tables.Project;
import org.jooq.generated.tables.records.ProjectRecord;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.jooq.generated.tables.Allocation.ALLOCATION;
import static org.jooq.generated.tables.Contract.CONTRACT;
import static org.jooq.generated.tables.Project.PROJECT;

@Repository
public class ProjectRepository extends GenericCrudRepository<ProjectDto, ProjectRecord, Project> {

    public ProjectRepository(DSLContext dslContext, Converter converter) {
        super(dslContext, converter, PROJECT);
    }

    public Optional<ProjectDto> saveProject(ProjectDto project) {
        project.setId(UUID.randomUUID());
        return createOne(project);
    }

    public List<ProjectDto> getProjects(LocalDate fromDate, LocalDate toDate, UUID projectManagerId) {
        // Be aware of overlapping start and end dates: https://stackoverflow.com/a/17014131
        Date startDate = getConverter().localDateToSqlDate(fromDate);
        Date endDate = getConverter().localDateToSqlDate(toDate);

        SelectConditionStep<Record> condition = getDslContext().select().from(PROJECT)
            .where(PROJECT.START_DATE.lessOrEqual(endDate)).and(PROJECT.END_DATE.greaterOrEqual(startDate))
            .and(projectManagerId != null ? PROJECT.PROJECT_MANAGER_ID.eq(projectManagerId) : DSL.noCondition());

        return getConverter().projectRecordListToProjectDtoList(condition.fetchInto(PROJECT));
    }

    public Optional<ProjectDto> getProjectById(UUID id) {
        return readOne(table -> table.ID.eq(id));
    }

    public Optional<ProjectDto> updateProject(ProjectDto project) {
        return updateOne(project);
    }

    public void deleteProject(UUID id) {
        deleteMany(table -> table.ID.eq(id));
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

    public Optional<ProjectDto> getProjectIfAssigned(UUID projectId, UUID employeeId) {
        SelectConditionStep<Record> condition = getDslContext().select().from(ALLOCATION)
            .join(PROJECT).on(ALLOCATION.PROJECT_ID.eq(PROJECT.ID))
            .join(CONTRACT).on(ALLOCATION.CONTRACT_ID.eq(CONTRACT.ID))
            .where(CONTRACT.EMPLOYEE_ID.eq(employeeId))
            .and(ALLOCATION.PROJECT_ID.eq(projectId));
        return Optional.of(mapRecordToDto(condition.fetchSingleInto(PROJECT)));
    }

    public List<ProjectDto> getAllAssignedProjects(LocalDate fromDate, LocalDate toDate, UUID employeeId) {
        // Be aware of overlapping start and end dates: https://stackoverflow.com/a/17014131
        Date startDate = getConverter().localDateToSqlDate(fromDate);
        Date endDate = getConverter().localDateToSqlDate(toDate);

        SelectSeekStep1<Record, Date> condition = getDslContext().select().from(ALLOCATION)
            .join(PROJECT).on(ALLOCATION.PROJECT_ID.eq(PROJECT.ID))
            .join(CONTRACT).on(ALLOCATION.CONTRACT_ID.eq(CONTRACT.ID))
            .where(PROJECT.START_DATE.lessOrEqual(endDate)).and(PROJECT.END_DATE.greaterOrEqual(startDate))
            .and(CONTRACT.EMPLOYEE_ID.eq(employeeId))
            .orderBy(PROJECT.START_DATE);

        return getConverter().projectRecordListToProjectDtoList(condition.fetchInto(PROJECT));
    }
}

