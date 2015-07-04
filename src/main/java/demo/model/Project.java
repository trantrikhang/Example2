package demo.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
@Entity
@Table(name="project")
public class Project extends Object{

    @Column(name="company_id")
    private Integer companyId;

    @Column(name="project_manager_id")
    private Integer projectManagerId;

    @Transient
    private List<Employee> listEmployeeProject;

    @Transient
    private List<Task> listTask;

    protected Project(){}

    public Project(String name, Integer companyId, Integer projectManagerId){
        super(name);
        this.setCompanyId(companyId);
        this.setProjectManagerId(projectManagerId);
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(Integer projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

    public List<Employee> getListEmployeeProject() {
        return listEmployeeProject;
    }

    public void setListEmployeeProject(List<Employee> listEmployeeProject) {
        this.listEmployeeProject = listEmployeeProject;
    }

    public List<Task> getListTask() {
        return listTask;
    }

    public void setListTask(List<Task> listTask) {
        this.listTask = listTask;
    }
}
