package demo.model;

import javax.persistence.Column;
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

    @Column(name="project_id")
    private Integer projectId;

    @Column(name="task_parent_id")
    private Integer taskParentId;

    @Transient
    private List<Task> taskChild;

    protected Task(){}

    public Task(String name, Integer projectId){
        super(name);
        this.setProjectId(projectId);
        this.setTaskParentId(null);
    }

    public Task(String name, Integer projectId, Integer taskParentId){
        super(name);
        this.setProjectId(projectId);
        this.setTaskParentId(taskParentId);
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getTaskParentId() {
        return taskParentId;
    }

    public void setTaskParentId(Integer taskParentId) {
        this.taskParentId = taskParentId;
    }

    public List<Task> getTaskChild() {
        return taskChild;
    }

    public void setTaskChild(List<Task> taskChild) {
        this.taskChild = taskChild;
    }
}
