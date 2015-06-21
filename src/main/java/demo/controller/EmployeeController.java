package demo.controller;

import demo.model.Company;
import demo.model.Employee;
import demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Khang on 18/06/2015.
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private  final EmployeeRepository employeeRepository;

    @Autowired

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @RequestMapping(value ="/view",method= RequestMethod.GET)
    public Employee viewEmployee(@RequestParam("employee_id")String employee_id) {
        Employee employee = employeeRepository.findOne(employee_id);
        return employee;
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public Employee addEmployee(@RequestParam("employee_id")String employee_id,
                              @RequestParam("name")String name){
        Employee employee = new Employee();
        employee.setEmployee_id(employee_id);
        employee.setName(name);
        employeeRepository.save(employee);
        return employee;
    }

    @RequestMapping(value="/update",method=RequestMethod.PUT)
    public Employee updateEmployee(@RequestParam("employee_id")String employee_id,
                                 @RequestParam("name")String name){
        Employee employee=employeeRepository.findOne(employee_id);
        employee.setEmployee_id(employee_id);
        employee.setName(name);
        employeeRepository.save(employee);
        return employee;
    }

    @RequestMapping(value="/del",method=RequestMethod.DELETE)
    public void delEmployee(@RequestParam("company_id") String employee_id){
        Employee employee = employeeRepository.findOne(employee_id);
        employeeRepository.delete(employee);
    }

}
