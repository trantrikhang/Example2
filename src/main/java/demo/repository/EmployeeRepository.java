package demo.repository;

import demo.model.Employee;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Khang on 18/06/2015.
 */

public interface EmployeeRepository extends CrudRepository<Employee,String> {
}
