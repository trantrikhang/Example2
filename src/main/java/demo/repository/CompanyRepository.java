package demo.repository;

import demo.model.Company;
import demo.model.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
@RestResource(path ="company")
public interface CompanyRepository extends CrudRepository<Company,String>{
    @RestResource(exported = false)
    @Query("select company from Company company where company.name=?1")
    Company findByName(String name);

    @RestResource(exported = false)
    @Query("select company from Company company where company.name like ?1")
    List<Company> findLikeName(String name);

    @RestResource(exported = false)
    @Query("select employee from Employee employee where employee.company.company_id=?1")
    List<Employee> listOfEmployeeByCompanyId(String employee_id);
}
