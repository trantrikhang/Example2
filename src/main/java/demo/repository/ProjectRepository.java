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
public interface ProjectRepository extends CrudRepository<Project,Integer> {
}
