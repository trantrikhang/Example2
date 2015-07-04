package demo.repository;

import demo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Khang on 01/07/2015.
 */
public interface JpaTaskRepository extends JpaRepository<Task,Integer> {
    List<Task> findByTaskParentId (Integer taskId);
    List<Task> findByProjectIdAndTaskParentIdIsNull (Integer projectId);
}
