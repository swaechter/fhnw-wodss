package ch.fhnw.wodss.webapplication.components.project;

import ch.fhnw.wodss.webapplication.utils.MemoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepository extends MemoryRepository<ProjectDto> {

    @Override
    public void setEntityId(ProjectDto entry, Long value) {
        entry.setId(value);
    }

}
