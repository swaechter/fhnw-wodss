package ch.fhnw.wodss.webapplication.components.project;

import ch.fhnw.wodss.webapplication.utils.MemoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepository extends MemoryRepository<Project> {

    @Override
    public void setEntityId(Project entry, Long value) {
        entry.setId(value);
    }

}
