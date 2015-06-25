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
    public Employee viewEmployee(@RequestParam("employeeId")String employeeId) {
        Employee employee = employeeRepository.findOne(employeeId);
        return employee;
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public Employee addEmployee(@RequestParam("employeeId")String employeeId,
                              @RequestParam("employeeName")String employeeName,
                                @RequestParam("company")Company company){
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setEmployeName(employeeName);
        employee.setCompany(company);
        employeeRepository.save(employee);
        return employee;
    }

    @RequestMapping(value="/update",method=RequestMethod.PUT)
    public Employee updateEmployee(@RequestParam("employeeId")String employeeId,
                                 @RequestParam("employeeName")String employeeName,
                                   @RequestParam("company")Company company){
        Employee employee=employeeRepository.findOne(employeeId);
        employee.setEmployeeId(employeeId);
        employee.setEmployeName(employeeName);
        employee.setCompany(company);
        employeeRepository.save(employee);
        return employee;
    }

    @RequestMapping(value="/del",method=RequestMethod.DELETE)
    public String delEmployee(@RequestParam("company_id") String employeeId){
        Employee employee = employeeRepository.findOne(employeeId);
        employeeRepository.delete(employee);
        String msg="deleted";
        System.out.println(msg);
        return msg;

    }

    @RequestMapping(value="/list",method=RequestMethod.GET)
    public Employee listEmployee(@RequestParam("companyId") String companyId){
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList=companyRepository.listEmployeeByCompanyId(companyId);
        Integer count = employeeList.size();
        for(int i=0;i<count;i++){
            return employeeList.get(i);
            //System.out.println(employeeList.get(i));
        }
        return employeeList.get(0);
    }

}
