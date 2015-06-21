package demo.controller;

import demo.model.Company;
import demo.model.Employee;
import demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by Quan Do on 6/17/2015.
 */
@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @RequestMapping(value ="/view",method= RequestMethod.GET)
    public Company viewCompany(@RequestParam("company_id")String company_id) {
        Company company = companyRepository.findOne(company_id);
        return company;
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public Company addCompany(@RequestParam("company_id")String company_id,
                              @RequestParam("name")String name){
        Company company = new Company();
        company.setCompany_id(company_id);
        company.setName(name);
        companyRepository.save(company);
        return company;
    }

    @RequestMapping(value="/update",method=RequestMethod.PUT)
    public Company updateCompany(@RequestParam("company_id")String company_id,
                              @RequestParam("name")String name){
        Company company=companyRepository.findOne(company_id);
        company.setCompany_id(company_id);
        company.setName(name);
        companyRepository.save(company);
        return company;
    }

    @RequestMapping(value="/del",method=RequestMethod.DELETE)
    public void delCompany(@RequestParam("company_id") String company_id){
        Company company = companyRepository.findOne(company_id);
        companyRepository.delete(company);
    }

    @RequestMapping(value="/list",method=RequestMethod.GET)
    public List<Employee> listEmployee(@RequestParam("company_id")String company_id){
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList.add(companyRepository.listOfEmployeeByCompanyId())
    }
}
