package demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Khang on 18/06/2015.
 */
@Entity
@Table(name = "employee")
public class Employee extends People {

    private String companyId;

    private String projectId;

    protected Employee(){}

    public Employee(String id, String name, String password, String salt){
        super(id,name,password,salt);
        this.companyId=null;
        this.projectId=null;
    }

    public Employee(String id, String name, String password, String salt, String companyId){
        super(id,name,password,salt);
        this.companyId=companyId;
        this.projectId=null;
    }

    public Employee(String id, String name, String password, String salt, String companyId, String projectId){
        super(id,name,password,salt);
        this.companyId=companyId;
        this.projectId=projectId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}