package demo.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
@Entity
@Table(name="company")
public class Company {
    @Id
    private String company_id;

    private String name;

    @OneToMany(mappedBy = "accountOwner",cascade = CascadeType.ALL,targetEntity = Project.class,fetch = FetchType.LAZY)
    private List<Employee> employeeList;

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}
