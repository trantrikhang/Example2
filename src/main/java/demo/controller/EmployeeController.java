package demo.controller;

import demo.model.Company;
import demo.model.Employee;
import demo.repository.CompanyRepository;
import demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private  final EmployeeRepository employeeRepository;

    @Autowired
    CompanyRepository companyRepository;
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
                              @RequestParam("name")String name,
                                @RequestParam("company")Company company){
        Employee employee = new Employee();
        employee.setEmployee_id(employee_id);
        employee.setName(name);
        employee.setCompany(company);
        employeeRepository.save(employee);
        return employee;
    }

    @RequestMapping(value="/update",method=RequestMethod.PUT)
    public Employee updateEmployee(@RequestParam("employee_id")String employee_id,
                                 @RequestParam("name")String name,
                                   @RequestParam("company")Company company){
        Employee employee=employeeRepository.findOne(employee_id);
        employee.setEmployee_id(employee_id);
        employee.setName(name);
        employee.setCompany(company);
        employeeRepository.save(employee);
        return employee;
    }

    @RequestMapping(value="/del",method=RequestMethod.DELETE)
    public String delEmployee(@RequestParam("company_id") String employee_id){
        Employee employee = employeeRepository.findOne(employee_id);
        employeeRepository.delete(employee);
        String msg="deleted";
        System.out.println(msg);
        return msg;

    }

    @RequestMapping(value="/list",method=RequestMethod.GET)
    public List<Employee> listEmployee(@RequestParam("company_id") String company_id){
        List<Employee> employeeList = new ArrayList<Employee>();
        Company company_find = companyRepository.findOne(company_id);
        employeeList=companyRepository.listOfEmployeeByCompanyId(company_find.getCompany_id());
        for(Employee employee_entry : employeeList){
            employee_entry.setEmployeeList(companyRepository.listOfEmployeeByCompanyId(company_id));
        }
        return employeeList;
    }

}
