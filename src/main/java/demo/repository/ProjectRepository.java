package demo.repository;

import demo.model.Employee;
import demo.model.Project;
import demo.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by Khang on 26/06/2015.
 */
@RestResource(path="project")
public interface ProjectRepository extends CrudRepository<Project,String> {

    @RestResource(exported = false)
    @Query("select project from Project project where project.companyId=?1")
    List<Project> listProjectByCompanyId(String companyId);

    @RestResource(exported = false)
    @Query("select project from Project project")
    List<Project> listAllProject();
}
