package demo.repository;

import demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Khang on 01/07/2015.
 */
public interface JpaTaskRepository extends JpaRepository<Task,String> {
    List<Task> findByTaskParentId (String taskId);
    List<Task> findByProjectIdAndTaskParentIdIsNull (String projectId);
}
