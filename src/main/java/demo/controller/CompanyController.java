package demo.controller;

import demo.model.*;
import demo.repository.*;
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

    @Autowired
    OwnerRepository ownerRepository;

    @Autowired
    EmployeeController employeeController;

    @Autowired
    ProjectController projectController;

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public Owner getOwner(@RequestParam("ownerId")String ownerId,
                          @RequestParam("ownerPassword")String ownerPassword){
        Owner owner = ownerRepository.getOwner(ownerId,ownerPassword);
        return owner;
    }

    @RequestMapping(value ="/view",method= RequestMethod.GET)
    public Company viewCompany(@RequestParam("companyId")String companyId) {
        Company company = companyRepository.findOne(companyId);
        company.setEmployeeList(employeeRepository.listEmployeeByCompanyId(companyId));
        company.setProjectList(projectRepository.listProjectByCompanyId(companyId));
        List<Project> projectList = projectRepository.listProjectByCompanyId(companyId);
        for(Project projectEntry : projectList){
            projectEntry.setListEmployeeProject(employeeRepository.listEmployeeByProjectId(projectEntry.getProjectId()));
            projectEntry.setListTask(taskRepository.listTaskByProjectId(projectEntry.getProjectId()));
            projectRepository.save(projectEntry);
            for(Task taskEntry : projectEntry.getListTask()){
                taskEntry.setTaskChild(taskRepository.listTaskChildByTaskId(taskEntry.getTaskParentId()));
                taskRepository.save(taskEntry);
            }
        }

        companyRepository.save(company);
        return company;
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public String addCompany(@RequestParam("companyId")String companyId,
                              @RequestParam("companyName")String companyName,
                              @RequestParam("ownerId")String ownerId,
                              @RequestParam("ownerPassword")String ownerPassword){
        Owner owner = ownerRepository.getOwner(ownerId,ownerPassword);
        if(owner==null) return "Wrong Owner's info";
        else{
            Company company = new Company();
            owner = ownerRepository.findOne(ownerId);
            company.setCompanyId(companyId);
            company.setCompanyName(companyName);
            owner.setCompanyId(companyId);
            companyRepository.save(company);
            ownerRepository.save(owner);
            return "Added";
        }

    }

    @RequestMapping(value="/update",method=RequestMethod.PUT)
    public String updateCompany(@RequestParam("companyId")String companyId,
                                 @RequestParam("companyName")String companyName,
                                 @RequestParam("ownerId")String ownerId,
                                 @RequestParam("ownerPassword")String ownerPassword){
        Owner owner = ownerRepository.getOwner(ownerId,ownerPassword);
        if(owner==null) return "Wrong Owner's info";
        else{
            Company company = companyRepository.findOne(companyId);
            owner = ownerRepository.findOne(ownerId);
            company.setCompanyName(companyName);
            owner.setCompanyId(companyId);
            companyRepository.save(company);
            ownerRepository.save(owner);
            return "Added";
        }
    }

    @RequestMapping(value="/del",method=RequestMethod.DELETE)
    public String delCompany(@RequestParam("companyId") String companyId,
                           @RequestParam("ownerId")String ownerId,
                           @RequestParam("ownerPassword")String ownerPassword) {
        Owner owner = ownerRepository.getOwner(ownerId, ownerPassword);
        if (owner == null) return "Wrong Owner's info";
        else {
            Company company = companyRepository.findOne(companyId);
            if (owner.getCompanyId() == company.getCompanyId()) {
                List<Project> projectList = projectRepository.listProjectByCompanyId(companyId);
                for (int i = 0; i < projectList.size(); i++) {
                    projectController.delProject(projectList.get(i).getProjectId());
                }
                List<Employee> employeeList = employeeRepository.listEmployeeByCompanyId(companyId);
                for (int j = 0; j < employeeList.size(); j++) {
                    employeeController.delEmployee(employeeList.get(j).getEmployeeId(), ownerId, ownerPassword);
                }
                owner.setCompanyId(null);
                companyRepository.delete(company);
                return "Deleted";
            }
            else return "Cannot delete this company";
        }
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
