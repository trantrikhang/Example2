package demo.repository;

import demo.model.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */

public interface EmployeeRepository extends CrudRepository<Employee,Integer> {
}
