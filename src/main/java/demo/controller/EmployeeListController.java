package demo.controller;

import demo.model.Employee;
import demo.model.EmployeeList;
import demo.repository.EmployeeListRepository;
import demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
@RestController
@RequestMapping("/employeelist")
public class EmployeeListController {
    private  final EmployeeListRepository employeeListRepository;
    @Autowired

    public EmployeeListController(EmployeeListRepository employeeListRepository) {
        this.employeeListRepository = employeeListRepository;
    }

    @RequestMapping(value ="/view",method= RequestMethod.GET)
    public EmployeeList viewEmployeeList(@RequestParam("company_id")String company_id) {
        EmployeeList employeeList = employeeListRepository.findOne(company_id);
        return employeeList;
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public EmployeeList addEmployee(@RequestParam("employee_id")String employee_id,
                                @RequestParam("company_id")String company_id){
        EmployeeList employeeList = new EmployeeList();
        employeeList.setEmployee_id(employee_id);
        employeeList.setCompany_id(company_id);
        employeeListRepository.save(employeeList);
        return employeeList;
    }

    @RequestMapping(value="/update",method=RequestMethod.PUT)
    public EmployeeList updateEmployeeList(@RequestParam("employee_id")String employee_id,
                                   @RequestParam("company_id")String company_id) {
        EmployeeList employeeList=employeeListRepository.findOne(employee_id);
        employeeList.setEmployee_id(employee_id);
        employeeList.setCompany_id(company_id);
        employeeListRepository.save(employeeList);
        return employeeList;
    }

    @RequestMapping(value="/del",method=RequestMethod.DELETE)
    public void delEmployeeList(@RequestParam("employee_id") String employee_id){
        EmployeeList employeeList = employeeListRepository.findOne(employee_id);
        employeeListRepository.delete(employeeList);
    }
    DataSource datasource;
    private JdbcTemplate jdbcTemplate;
    public void setDataSource(DataSource datasource) {


        this.datasource = datasource;
        this.jdbcTemplate = new JdbcTemplate(datasource);

    }
    @RequestMapping(value="/list",method=RequestMethod.POST)
    public List<EmployeeList> listEmployeeList(@RequestParam("company_id")String company_id){
        List<EmployeeList> listEmployee=new ArrayList<EmployeeList>();




        return listEmployee;
    }

}
