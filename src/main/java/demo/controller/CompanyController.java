package demo.controller;

import demo.model.Company;
import demo.model.Employee;
import demo.model.Project;
import demo.model.Task;
import demo.repository.CompanyRepository;
import demo.repository.EmployeeRepository;
import demo.repository.ProjectRepository;
import demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @RequestMapping(value ="/view",method= RequestMethod.GET)
    public Company viewCompany(@RequestParam("companyId")String companyId) {
        Company company = companyRepository.findOne(companyId);
        company.setEmployeeList(employeeRepository.listEmployeeByCompanyId(companyId));
        company.setProjectList(projectRepository.listProjectByCompanyId(companyId));
        List<Project> projectList = projectRepository.listProjectByCompanyId(companyId);
        for(Project projectEntry : projectList){
            projectEntry.setListEmployeeProject(employeeRepository.listEmployeeByProjectId(projectEntry.getProjectId()));
            projectRepository.save(projectEntry);
            List<Task> taskList = taskRepository.listTaskByProjectId(projectEntry.getProjectId());
            for(Task taskEntry : taskList){
                taskEntry.setTaskChild(taskRepository.listTaskChildByTaskId(taskEntry.getTaskParentId()));
                taskRepository.save(taskEntry);
            }
        }

        companyRepository.save(company);
        return company;
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public Company addCompany(@RequestParam("companyId")String companyId,
                              @RequestParam("companyName")String companyName){
        Company company = new Company();
        company.setCompanyId(companyId);
        company.setCompanyName(companyName);
        companyRepository.save(company);
        return company;
    }

    @RequestMapping(value="/update",method=RequestMethod.PUT)
    public Company updateCompany(@RequestParam("companyId")String companyId,
                                 @RequestParam("companyName")String companyName){
        Company company=companyRepository.findOne(companyId);
        company.setCompanyId(companyId);
        company.setCompanyName(companyName);
        companyRepository.save(company);
        return company;
    }

    @RequestMapping(value="/del",method=RequestMethod.DELETE)
    public void delCompany(@RequestParam("companyId") String companyId){
        Company company = companyRepository.findOne(companyId);
        companyRepository.delete(company);
    }

    @RequestMapping(value="/listAll",method=RequestMethod.GET)
    public List<Company> listAllCompany(){
        List<Company> companyList = new ArrayList<Company>();
        companyList=companyRepository.listAllCompany();
        for(Company companyEntry : companyList){
            viewCompany(companyEntry.getCompanyId());
        }
        return companyList;
    }

}
