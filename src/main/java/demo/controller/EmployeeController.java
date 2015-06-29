package demo.controller;

import demo.model.Company;
import demo.model.Employee;
import demo.model.Project;
import demo.repository.CompanyRepository;
import demo.repository.EmployeeRepository;
import demo.repository.ProjectRepository;
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

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    @RequestMapping(value ="/view",method= RequestMethod.GET)
    public Employee viewEmployee(@RequestParam("employeeId")String employeeId) {
        Employee employee = employeeRepository.findOne(employeeId);
        return employee;
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public String addEmployee(@RequestParam("employeeId")String employeeId,
                              @RequestParam("employeeName")String employeeName,
                                @RequestParam("companyId")String companyId,
                                @RequestParam("projectId") String projectId){
        Employee employee = new Employee();
        Company company = companyRepository.findOne(companyId);
        employee.setEmployeeId(employeeId);
        employee.setEmployeeName(employeeName);
        if(companyRepository.exists(companyId)){
            employee.setCompanyId(companyId);
        }
        else return "Wrong companyId";
        if(projectRepository.exists(projectId)){
            employee.setProjectId(projectId);
        }
        else return "Wrong projectId";

        employeeRepository.save(employee);

        return "Added";
    }

    @RequestMapping(value="/update",method=RequestMethod.PUT)
    public String updateEmployee(@RequestParam("employeeId")String employeeId,
                                   @RequestParam("employeeName")String employeeName,
                                   @RequestParam("companyId")String companyId,
                                   @RequestParam("projectId") String projectId){
        Employee employee=employeeRepository.findOne(employeeId);
        employee.setEmployeeId(employeeId);
        employee.setEmployeeName(employeeName);
        if(companyRepository.exists(companyId)){
            employee.setCompanyId(companyId);
        }
        else return "Wrong companyId";
        if(projectRepository.exists(projectId)){
            employee.setProjectId(projectId);
        }
        else return "Wrong projectId";

        employeeRepository.save(employee);

        return "Updated";
    }

    /*@RequestMapping(value="/del",method=RequestMethod.DELETE)
    public String delEmployee(@RequestParam("employeeId") String employeeId){
        Employee employee = employeeRepository.findOne(employeeId);
        Project project = projectRepository.findOne(employee.getProject().getProjectId());

        employeeRepository.delete(employee);
        String msg="deleted";
        System.out.println(msg);
        return msg;

    }*/

    @RequestMapping(value="/listAll", method=RequestMethod.GET)
    public List<Employee> listAllEmployee(){
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList=employeeRepository.listAllEmployee();
        return employeeList;
    }
    @RequestMapping(value="/listByCompanyId",method=RequestMethod.GET)
    public List<Employee> listEmployee(@RequestParam("companyId") String companyId){
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList=employeeRepository.listEmployeeByCompanyId(companyId);
        return employeeList;
    }

    @RequestMapping(value="/listByProjectId",method=RequestMethod.GET)
    public List<Employee> listProjectEmployee(@RequestParam("projectId")String projectId){
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList=employeeRepository.listEmployeeByProjectId(projectId);
        return employeeList;
    }
}
