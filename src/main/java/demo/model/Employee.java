package demo.model;

import javax.persistence.*;

/**
 * Created by Khang on 18/06/2015.
 */
@Entity
@Table(name = "employee")
public class Employee extends People {

    @Column(name="company_id")
    private Integer companyId;

    @Column(name="project_id")
    private Integer projectId;

    protected Employee(){}

    public Employee(String name, String password){
        super(name,password);
        this.companyId=null;
        this.projectId=null;
    }

    public Employee(String name, String password, Integer companyId){
        super(name,password);
        this.companyId=companyId;
        this.projectId=null;
    }

    public Employee(String name, String password, Integer companyId, Integer projectId){
        super(name,password);
        this.companyId=companyId;
        this.projectId=projectId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}