package demo.repository;

import demo.model.Company;
import demo.model.Employee;
import demo.model.Project;
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
    @Query("select company from company company where company.companyName=?1")
    Company findByName(String companyName);

    @RestResource(exported = false)
    @Query("select company from company company where company.companyName like ?1")
    List<Company> findLikeName(String companyName);

    @RestResource(exported = false)
    @Query("select employee from employee employee where employee.company.companyId=?1")
    List<Employee> listEmployeeByCompanyId(String companyId);

    @RestResource(exported = false)
    @Query("select project from project project where project.company_owner.companyId=?1")
    List<Project> listProjectByCompanyId(String companyId);

}
