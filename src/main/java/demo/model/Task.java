package demo.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
@Entity
@Table(name="task")
public class Task extends Object {

    private String projectId;

    private String taskParentId;

    @Transient
    private List<Task> taskChild;

    protected Task(){}

    public Task(String id, String name, String projectId){
        super(id,name);
        this.setProjectId(projectId);
        this.setTaskParentId(null);
    }

    public Task(String id, String name, String projectId, String taskParentId){
        super(id,name);
        this.setProjectId(projectId);
        this.setTaskParentId(taskParentId);
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
