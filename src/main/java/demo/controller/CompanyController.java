package demo.controller;

import com.sun.javafx.tk.Toolkit;
import demo.bean.CompanyReturnValue;
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
    @Autowired
    TaskController taskController;


    @RequestMapping(value ="/view",method= RequestMethod.GET)
    public CompanyReturnValue viewCompany(@RequestParam("companyId")Integer companyId) {
        Company company = companyRepository.findOne(companyId);
        CompanyReturnValue returnValue;
        if(company==null){
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_NOT_EXIST);
            return returnValue;
        }
        else {
            //company.setEmployeeList(employeeController.listEmployeeByCompanyId(companyId));
            //company.setProjectList(projectController.listByCompanyId(companyId));
            employeeController.listEmployeeByCompanyId(companyId);
            List<Project> projectList = jpaProjectRepository.findByCompanyId(companyId);
            for(Project projectEntry : projectList){
                projectController.viewProject(projectEntry.getCompanyId());
            }
            projectRepository.save(projectList);
            companyRepository.save(company);
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_VALID);
            returnValue.setCompany(company);
            returnValue.setCompanyList(null);
            return returnValue;
        }
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public CompanyReturnValue addCompany(@RequestParam("companyName")String companyName,
                             @RequestParam("ownerId")Integer ownerId,
                             HttpSession session) {
        CompanyReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        Company company = new Company();
        if(owner==null) {
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null){
                returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            }//session null > chưa login
            else {
                Owner ownerSession = (Owner) session.getAttribute("ownerSession");
                if (ownerSession.getId().equals(owner.getId())) {//ownerId vừa nhập nếu trùng với ownerId trong session thì cho phép thực thi
                    if (owner.getCompanyId() != null) {
                        returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNER_ONLY_OWN_1_COMPANY);
                        return returnValue;
                    }//mỗi tài khoản owner chỉ được phép tạo 1 company
                    else {
                        company.setName(companyName);
                        company.setOwnerId(ownerId);
                        companyRepository.save(company);
                        owner.setCompanyId(company.getId());
                        ownerRepository.save(owner);
                        returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_VALID);
                        returnValue.setCompany(company);
                        returnValue.setCompanyList(null);
                        return returnValue;
                    }
                } else{
                    returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }
    @RequestMapping(value="/update",method=RequestMethod.POST)
    public CompanyReturnValue updateCompany(@RequestParam("companyId")Integer companyId,
                                @RequestParam("companyName")String companyName,
                                @RequestParam("ownerId")Integer ownerId,
                                HttpSession session) {
        CompanyReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if(owner==null){
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null){
                returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            }
            else {
                Owner ownerSession = (Owner) session.getAttribute("ownerSession");
                if (ownerSession.getId().equals(owner.getId())) {
                    Company company = companyRepository.findOne(companyId);
                    if (company == null){
                        returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_EXISTED);
                        return returnValue;
                    }//company không tồn tại
                    else {
                        company.setName(companyName);
                        company.setOwnerId(ownerId);
                        owner.setCompanyId(companyId);
                        companyRepository.save(company);
                        ownerRepository.save(owner);
                        returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_VALID);
                        returnValue.setCompany(company);
                        returnValue.setCompanyList(null);
                        return returnValue;
                    }
                } else{
                    returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/del",method=RequestMethod.POST)
    public CompanyReturnValue delCompany(@RequestParam("companyId") Integer companyId,
                             @RequestParam("ownerId")Integer ownerId,
                             HttpSession session) {
        Owner owner = ownerRepository.findOne(ownerId);
        CompanyReturnValue returnValue;
        if (owner == null) {
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null) {
                returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            } else {
                Owner ownerSession = (Owner) session.getAttribute("ownerSession");
                if (ownerSession.getId().equals(owner.getId())) {
                    Company company = companyRepository.findOne(companyId);
                    if (company == null) {
                        returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_NOT_EXIST);
                        return returnValue;
                    } else {
                        if (owner.getCompanyId().equals(company.getId())) {
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
                                            }
                                            else {
                                                returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_TASKCHILDLIST_EMPTY);
                                                return returnValue;
                                            }
                                            taskRepository.delete(taskList.get(j));
                                        }
                                    }
                                    else {
                                        returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_TASKLIST_EMPTY);
                                        return returnValue;
                                    }
                                    projectRepository.delete(projectList.get(i));
                                }
                            }
                            else {
                                returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_PROJECTLIST_EMPTY);
                                return returnValue;
                            }
                            List<Employee> employeeList = jpaEmployeeRepository.findByCompanyId(companyId);
                            if (employeeList != null) {
                                for (int i = 0; i < employeeList.size(); i++) {
                                    employeeList.get(i).setCompanyId(null);
                                    employeeRepository.save(employeeList.get(i));
                                }
                            } else {
                                returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEELIST_EMPTY);
                                return returnValue;
                            }
                            owner.setCompanyId(null);
                            companyRepository.delete(company);
                            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_VALID);
                            returnValue.setCompany(null);
                            returnValue.setCompanyList(null);
                            return returnValue;
                        } else {
                            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_OWN_THIS_COMPANY);
                            return returnValue;
                        }
                    }
                } else {
                    returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/listAll",method=RequestMethod.GET)
    public CompanyReturnValue listAllCompany(){
        CompanyReturnValue returnValue;
        List<Company> companyList = (List<Company>) companyRepository.findAll();
        if(companyList==null){
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_COMPANYLIST_EMPTY);
            returnValue.setCompany(null);
            returnValue.setCompanyList(null);
            return returnValue;
        }
        else {
            for (Company companyEntry : companyList) {
                viewCompany(companyEntry.getId());
            }
            companyRepository.save(companyList);
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_VALID);
            returnValue.setCompanyList(companyList);
            returnValue.setCompany(null);
            return returnValue;
        }
    }

}
