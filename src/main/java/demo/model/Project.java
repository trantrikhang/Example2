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
@Table(name="project")
public class Project extends Object{

    private String companyId;

    private String projectManagerId;

    @Transient
    private List<Employee> listEmployeeProject;

    @Transient
    private List<Task> listTask;

    protected Project(){}

    public Project(String id, String name, String companyId, String projectManagerId){
        super(id,name);
        this.setCompanyId(companyId);
        this.setProjectManagerId(projectManagerId);
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public String getProjectManagerId() {
        return projectManagerId;
    }

    public void setProjectManagerId(String projectManagerId) {
        this.projectManagerId = projectManagerId;
    }
}
