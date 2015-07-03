package demo.repository;

import demo.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Khang on 01/07/2015.
 */
public interface JpaEmployeeRepository extends JpaRepository<Employee,String> {
    List<Employee> findByProjectId (String projectId);
    List<Employee> findByCompanyId (String companyId);
}
