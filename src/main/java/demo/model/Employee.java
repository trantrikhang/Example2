package demo.model;

import javax.persistence.*;

/**
 * Created by Khang on 18/06/2015.
 */
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    private String employeeId;

    private String employeName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "companyId",referencedColumnName = "companyId")
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId",referencedColumnName = "projectId")
    private Project project;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeName() {
        return employeName;
    }

    public void setEmployeName(String employeName) {
        this.employeName = employeName;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

}