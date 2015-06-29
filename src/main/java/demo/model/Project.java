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
public class Project {
    @Id
    private String projectId;

    private String projectName;

    private String companyId;

    @Transient
    private List<Employee> listEmployeeProject;

    @Transient
    private List<Task> listTask;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
}
