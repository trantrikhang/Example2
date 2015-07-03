package demo.controller;

import demo.model.*;
import demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/company")
@SessionAttributes({"ownerSession","employeeSession"})
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
    @Autowired
    JpaEmployeeRepository jpaEmployeeRepository;
    @Autowired
    JpaProjectRepository jpaProjectRepository;
    @Autowired
    JpaTaskRepository jpaTaskRepository;



    @RequestMapping(value ="/view",method= RequestMethod.GET)
    public CompanyReturnValue viewCompany(@RequestParam("companyId")String companyId) {
        Company company = companyRepository.findOne(companyId);
        HashMap errorList = new HashMap();
        CompanyReturnValue returnValue;
        if(company==null){
            errorList.put(-300, "companyId not exist");
            returnValue = new CompanyReturnValue((Company) null, errorList);
            return returnValue;
        }
        else {
            //company.setEmployeeList(employeeController.listEmployeeByCompanyId(companyId));
            //company.setProjectList(projectController.listByCompanyId(companyId));
            companyRepository.save(company);
            errorList.put(1, "list successful");
            returnValue = new CompanyReturnValue(company, errorList);
            return returnValue;
        }
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public CompanyReturnValue addCompany(@RequestParam("companyId")String companyId,
                             @RequestParam("companyName")String companyName,
                             @RequestParam("ownerId")String ownerId,
                             HttpSession session) {
        HashMap errorList = new HashMap();
        CompanyReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        Company company = new Company();
        if(owner==null) {
            errorList.put(-200, "ownerId not exist");
            returnValue = new CompanyReturnValue((Company) null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null){
                errorList.put(-100, "must login first");
                returnValue = new CompanyReturnValue((Company) null, errorList);
                return returnValue;
            }//session null > chưa login
            else {
                if (session.getAttribute("ownerSession").equals(owner.getId())) {//ownerId vừa nhập nếu trùng với ownerId trong session thì cho phép thực thi
                    if (owner.getCompanyId() != null){
                        errorList.put(-201, "Owner only can own 1 company");
                        returnValue = new CompanyReturnValue((Company) null, errorList);
                        return returnValue;
                    }//mỗi tài khoản owner chỉ được phép tạo 1 company
                    else {
                        if (companyRepository.exists(companyId)){
                            errorList.put(-300, "companyId existed");
                            returnValue = new CompanyReturnValue((Company) null, errorList);
                            return returnValue;
                        }//companyId không được trùng với cái sẵn có
                        else {
                            company.setCompanyId(companyId);
                            company.setCompanyName(companyName);
                            company.setOwnerId(ownerId);
                            owner.setCompanyId(companyId);
                            companyRepository.save(company);
                            ownerRepository.save(owner);
                            errorList.put(2, "added successful");
                            returnValue = new CompanyReturnValue(company, errorList);
                            return returnValue;
                        }
                    }
                } else{
                    errorList.put(-202, "ownerId not login");
                    returnValue = new CompanyReturnValue((Company) null, errorList);
                    return returnValue;
                }
            }
        }
    }
    @RequestMapping(value="/update",method=RequestMethod.PUT)
    public CompanyReturnValue updateCompany(@RequestParam("companyId")String companyId,
                                @RequestParam("companyName")String companyName,
                                @RequestParam("ownerId")String ownerId,
                                HttpSession session) {
        HashMap errorList = new HashMap();
        CompanyReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if(owner==null){
            errorList.put(-200, "ownerId not exist");
            returnValue = new CompanyReturnValue((Company) null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null){
                errorList.put(-100, "must login first");
                returnValue = new CompanyReturnValue((Company) null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("ownerSession").equals(owner.getId())) {
                    Company company = companyRepository.findOne(companyId);
                    if (company == null){
                        errorList.put(-300, "companyId existed");
                        returnValue = new CompanyReturnValue((Company) null, errorList);
                        return returnValue;
                    }//company không tồn tại
                    else {
                        company.setCompanyName(companyName);
                        company.setOwnerId(ownerId);
                        owner.setCompanyId(companyId);
                        companyRepository.save(company);
                        ownerRepository.save(owner);
                        errorList.put(3, "updated successful");
                        returnValue = new CompanyReturnValue(company, errorList);
                        return returnValue;
                    }
                } else{
                    errorList.put(-202, "ownerId not login");
                    returnValue = new CompanyReturnValue((Company) null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/del",method=RequestMethod.DELETE)
    public CompanyReturnValue delCompany(@RequestParam("companyId") String companyId,
                             @RequestParam("ownerId")String ownerId,
                             HttpSession session) {
        Owner owner = ownerRepository.findOne(ownerId);
        HashMap errorList = new HashMap();
        CompanyReturnValue returnValue = new CompanyReturnValue();
        if (owner == null) {
            errorList.put(-200, "ownerId not exist");
            returnValue = new CompanyReturnValue((Company) null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null) {
                errorList.put(-100, "must login first");
                returnValue = new CompanyReturnValue((Company) null, errorList);
                return returnValue;
            } else {
                if (session.getAttribute("ownerSession").equals(owner.getId())) {
                    Company company = companyRepository.findOne(companyId);
                    if (company == null) {
                        errorList.put(-300, "companyId not existed");
                        returnValue = new CompanyReturnValue((Company) null, errorList);
                        return returnValue;
                    } else {
                        if (owner.getCompanyId().equals(company.getCompanyId())) {
                            List<Project> projectList = jpaProjectRepository.findByCompanyId(companyId);
                            if (projectList != null) {
                                for (int i = 0; i < projectList.size(); i++) {
                                    List<Task> taskList = jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectList.get(i).getId());
                                    if (taskList != null) {
                                        for (int j = 0; j < taskList.size(); j++) {
                                            List<Task> taskChildList = jpaTaskRepository.findByTaskParentId(taskList.get(j).getTaskParentId());
                                            if (taskChildList != null) {
                                                for (int k = 0; k < taskChildList.size(); k++) {
                                                    taskRepository.delete(taskChildList.get(k));
                                                }
                                            } else {
                                                errorList.put(-500, "taskChildList is empty");
                                            }
                                            taskRepository.delete(taskList.get(j));
                                        }
                                    } else {
                                        errorList.put(-501, "taskList is empty");
                                    }
                                    projectRepository.delete(projectList.get(i));
                                }
                            } else {
                                errorList.put(-400, "projectList is empty");
                            }
                            List<Employee> employeeList = jpaEmployeeRepository.findByCompanyId(companyId);
                            if (employeeList != null) {
                                for (int i = 0; i < employeeList.size(); i++) {
                                    employeeList.get(i).setCompanyId(null);
                                    employeeRepository.save(employeeList.get(i));
                                }
                            } else {
                                errorList.put(-600, "employeeList is empty");
                                returnValue = new CompanyReturnValue((Company) null, errorList);
                                return returnValue;
                            }
                            owner.setCompanyId(null);
                            companyRepository.delete(company);
                            errorList.put(4, "deleted successful");
                            returnValue = new CompanyReturnValue(company, errorList);
                            return returnValue;
                        } else {
                            errorList.put(-203, "ownerId not own this company");
                            returnValue = new CompanyReturnValue((Company) null, errorList);
                            return returnValue;
                        }
                    }
                } else {
                    errorList.put(-202, "ownerId not login");
                    returnValue = new CompanyReturnValue((Company) null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/listAll",method=RequestMethod.GET)
    public Iterable<Company> listAllCompany(){
//        HashMap errorList = new HashMap();
//        CompanyReturnValue returnValue;
        Iterable<Company> companyList = companyRepository.findAll();
//        if(companyList==null){
//            errorList.put(-302, "companyList is empty");
//            returnValue = new CompanyReturnValue((Iterable<Company>) null, errorList);
//            return returnValue;
//        }
//        else {
            for (Company companyEntry : companyList) {
                viewCompany(companyEntry.getCompanyId());
            }
//            errorList.put(1, "list successful");
//            returnValue = new CompanyReturnValue(companyList, errorList);
//            return returnValue;
//        }
        return companyList;
    }

}
