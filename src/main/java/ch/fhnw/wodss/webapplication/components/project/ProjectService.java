package ch.fhnw.wodss.webapplication.components.project;

import ch.fhnw.wodss.webapplication.components.employee.EmployeeDto;
import ch.fhnw.wodss.webapplication.components.employee.EmployeeRepository;
import ch.fhnw.wodss.webapplication.components.employee.Role;
import ch.fhnw.wodss.webapplication.exceptions.EntityNotFoundException;
import ch.fhnw.wodss.webapplication.exceptions.InternalException;
import ch.fhnw.wodss.webapplication.exceptions.InvalidActionException;
import org.jooq.generated.tables.Employee;
import org.jooq.generated.tables.Project;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ProjectService {

    public static final String NO_PERMISSION_TO_CREATE_PROJECT =
        "No permission to create a " + "project";
    public static final String NO_PERMISSION_TO_UPDATE_PROJECT =
        "No permission to update a " + "project";
    public static final String EMPLOYEE_NOT_ACTIVATED =
        "The authenticated employee is " + "not activated";
    public static final String NO_PERMISSION_TO_DELETE_PROJECT =
        "No permission to delete a " + "project";
    private final ProjectRepository projectRepository;

    private final EmployeeRepository employeeRepository;

    public ProjectService(
        ProjectRepository projectRepository,
        EmployeeRepository employeeRepository)
    {
        this.projectRepository = projectRepository;
        this.employeeRepository = employeeRepository;
    }

    public ProjectDto createProject(
        ProjectDto project,
        EmployeeDto employee)
    {
        EmployeeDto found = employeeRepository.getEmployeeById(employee.getId())
            .orElseThrow(() -> new EntityNotFoundException("employee",
                                                           employee.getId()));
        if (!found.isAdministrator())
        {
            throw new InvalidActionException(NO_PERMISSION_TO_CREATE_PROJECT);
        }

        if (!found.isActive())
        {
            throw new InvalidActionException(EMPLOYEE_NOT_ACTIVATED);
        }


        Optional<ProjectDto> createdProject =
            projectRepository.saveProject(project);

        if (createdProject.isEmpty())
        {
            throw new InternalException("Unable to create the project");
        }

        return createdProject.get();
    }

    public List<ProjectDto> getProjects(
        LocalDate fromDate,
        LocalDate toDate,
        UUID projectManagerId,
        EmployeeDto employee)
    {
        EmployeeDto found = employeeRepository.getEmployeeById(employee.getId())
            .orElseThrow(() -> new EntityNotFoundException("employee",
                                                           employee.getId()));
        if (!found.isActive())
        {
            throw new InvalidActionException(EMPLOYEE_NOT_ACTIVATED);
        }

        return projectRepository.getProjects(fromDate,
                                             toDate,
                                             projectManagerId);
    }

    public ProjectDto getProject(
        UUID id,
        EmployeeDto employee)
    {
        EmployeeDto found = employeeRepository.getEmployeeById(employee.getId())
            .orElseThrow(() -> new EntityNotFoundException("employee",
                                                           employee.getId()));
        if (!found.isActive())
        {
            throw new InvalidActionException(EMPLOYEE_NOT_ACTIVATED);
        }

        return projectRepository.getProjectById(id)
            .orElseThrow(() -> new EntityNotFoundException("project", id));
    }

    public ProjectDto updateProject(
        ProjectDto project,
        EmployeeDto employee)
    {
        EmployeeDto foundEmployee =
            employeeRepository.getEmployeeById(employee.getId())
                .orElseThrow(() -> new EntityNotFoundException("employee",
                                                               employee.getId()));
        if (foundEmployee.isDeveloper())
        {
            throw new InvalidActionException(NO_PERMISSION_TO_UPDATE_PROJECT);
        }

        if (!foundEmployee.isActive())
        {
            throw new InvalidActionException(EMPLOYEE_NOT_ACTIVATED);
        }

        ProjectDto foundProject =
            projectRepository.getProjectById(project.getId())
                .orElseThrow(() -> new EntityNotFoundException("project",
                                                               project.getId()));


        Optional<ProjectDto> updatedProject =
            projectRepository.updateProject(project);
        if (updatedProject.isEmpty())
        {
            throw new InternalException("Unable to update the project");
        }

        return updatedProject.get();
    }


    public void deleteProject(
        UUID id,
        EmployeeDto employee)
    {
        EmployeeDto foundEmployee =
            employeeRepository.getEmployeeById(employee.getId())
                .orElseThrow(() -> new EntityNotFoundException("employee",
                                                               employee.getId()));

        if (!foundEmployee.isActive())
        {
            throw new InvalidActionException(EMPLOYEE_NOT_ACTIVATED);
        }

        if (!foundEmployee.isAdministrator())
        {
            throw new InvalidActionException(NO_PERMISSION_TO_DELETE_PROJECT);
        }

        projectRepository.getProjectById(id)
            .orElseThrow(() -> new EntityNotFoundException("project", id));
        projectRepository.deleteProject(id);
    }
}
