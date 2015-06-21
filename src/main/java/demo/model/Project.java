package demo.model;

import org.springframework.context.annotation.EnableLoadTimeWeaving;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
@Entity
@Table(name="project")
public class Project {
    @Id
    private String project_id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id",referencedColumnName = "employee_id")
    private Employee manager_id;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,targetEntity = Task.class,fetch = FetchType.LAZY)
    private List<Task> listOfTask;

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getManager_id() {
        return manager_id;
    }

    public void setManager_id(Employee manager_id) {
        this.manager_id = manager_id;
    }

    public List<Task> getListOfTask() {
        return listOfTask;
    }

    public void setListOfTask(List<Task> listOfTask) {
        this.listOfTask = listOfTask;
    }
}
