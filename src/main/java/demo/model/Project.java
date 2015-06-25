package demo.model;

import javax.persistence.*;
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
    @OneToMany(mappedBy = "project")
    private List<Employee> listEmployeeProject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "companyId",referencedColumnName = "companyId")
    private Company companyOwner;

    @Transient
    @OneToMany(mappedBy = "project")
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

    public void setListEmployeeProject(List<Employee> listEmployeeProject) {
        this.listEmployeeProject = listEmployeeProject;
    }

    public List<Task> getListTask() {
        return listTask;
    }

    public void setListTask(List<Task> listTask) {
        this.listTask = listTask;
    }


    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }


    public Company getCompanyOwner() {
        return companyOwner;
    }

    public void setCompanyOwner(Company companyOwner) {
        this.companyOwner = companyOwner;
    }

    public void addEmployeeToProject(Employee employee){
        this.listEmployeeProject.add(employee);
    }
    public  void addTaskToProject(Task task){
        this.listTask.add(task);
    }
}
