package demo.repository;

import demo.model.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */

public interface EmployeeRepository extends CrudRepository<Employee,String> {
    @Query("select employee from Employee employee")
    List<Employee> listAllEmployee();

    @RestResource(exported = false)
    @Query("select employee from Employee employee where employee.projectId =?1")
    List<Employee> listEmployeeByProjectId(String projectId);

    @RestResource(exported = false)
    @Query("select employee from Employee employee where employee.companyId=?1")
    List<Employee> listEmployeeByCompanyId(String companyId);

}
