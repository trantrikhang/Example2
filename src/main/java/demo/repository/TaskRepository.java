package demo.repository;

import demo.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
public interface TaskRepository extends CrudRepository<Task,String> {
    @Query("select task from Task task where task.taskParent.task_id =?1")
    List<Task> getListOfTaskChildById(Integer taskId);
}
