package demo.repository;

import demo.model.Employee;
import demo.model.Project;
import demo.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
public interface ProjectRepository extends CrudRepository<Project,String> {
    @Query("select project from project project where project.projectName like ?1")
    List<Project> findByName(String name);

    @Query("select task from task task where task.project.projectId =?1")
    List<Task> getListOfTaskById(String projectId);

    @Query("select employee from employee employee where employee.project.projectId =?1")
    List<Employee> getListOfEmployeeById(String projectId);
}
