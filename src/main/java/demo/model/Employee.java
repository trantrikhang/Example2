package demo.model;

import javax.persistence.*;

/**
 * Created by Khang on 18/06/2015.
 */
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    private String employee_id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id",referencedColumnName = "company_id")
    private Company company;

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}