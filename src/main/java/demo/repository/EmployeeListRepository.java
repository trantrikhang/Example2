package demo.repository;

import demo.model.Employee;
import demo.model.EmployeeList;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Khang on 18/06/2015.
 */
public interface EmployeeListRepository extends CrudRepository<EmployeeList,String> {
}
