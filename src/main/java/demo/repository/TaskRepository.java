package demo.repository;

import demo.model.Project;
import demo.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
@RestResource(path="task")
public interface TaskRepository extends CrudRepository<Task,String> {
    //@RestResource(exported = false)
    @Query("select task from Task task where task.taskParentId =?1")
    List<Task> listTaskChildByTaskId(String taskId);

    @RestResource(exported = false)
    @Query("select task from Task task where task.projectId =?1")
    List<Task> listTaskByProjectId(String projectId);

}
