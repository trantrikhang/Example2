package demo.model;

import javax.persistence.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId",referencedColumnName = "projectId")
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="parentTaskId",referencedColumnName = "taskId")
    private Task taskParent;

    @Transient
    @OneToMany(mappedBy = "taskParent")
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Task getTaskParent() {
        return taskParent;
    }

    public void setTaskParent(Task taskParent) {
        this.taskParent = taskParent;
    }

    public List<Task> getTaskChild() {
        return taskChild;
    }

    public void setTaskChild(List<Task> taskChild) {
        this.taskChild = taskChild;
    }

}
