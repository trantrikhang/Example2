package demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
@Entity
@Table(name="task")
public class Task {
    @Id
    private String taskId;

    private String taskName;

    private String projectId;

    private String taskParentId;

    @Transient
    private List<Task> taskChild;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTaskParentId() {
        return taskParentId;
    }

    public void setTaskParentId(String taskParentId) {
        this.taskParentId = taskParentId;
    }

    public List<Task> getTaskChild() {
        return taskChild;
    }

    public void setTaskChild(List<Task> taskChild) {
        this.taskChild = taskChild;
    }
}
