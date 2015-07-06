package demo.controller;

import demo.bean.CompanyReturnValue;
import demo.model.*;
import demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
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
    public CompanyReturnValue viewCompany(@RequestParam("companyId")String companyIdInput) {
        CompanyReturnValue returnValue;
        if(companyIdInput.isEmpty()){
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_EMPTY);
            return returnValue;
        }
        else {
            int companyId = Integer.parseInt(companyIdInput);
            Company company = companyRepository.findOne(companyId);
            if (company == null) {
                returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_NOT_EXIST);
                return returnValue;
            } else {
                company.setEmployeeList(jpaEmployeeRepository.findByCompanyId(companyId));
                company.setProjectList(jpaProjectRepository.findByCompanyId(companyId));
                companyRepository.save(company);
                for (Project projectEntry : company.getProjectList()) {
                    projectEntry.setListTask(jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectEntry.getId()));
                    projectRepository.save(projectEntry);
                    for (Task taskEntry : projectEntry.getListTask()) {
                        taskEntry.setTaskChild(jpaTaskRepository.findByTaskParentId(taskEntry.getId()));
                        taskRepository.save(taskEntry);
                    }
                }
                companyRepository.save(company);
                returnValue = new CompanyReturnValue(company, GeneralConstant.RESULT_CODE_VALID);
                return returnValue;
            }
        }
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public CompanyReturnValue addCompany(@RequestParam("companyName")String companyName,
                             @RequestParam("ownerId")String ownerIdInput,
                             HttpSession session) {
        CompanyReturnValue returnValue;
        if(ownerIdInput.isEmpty()){
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_EMPTY);
            return returnValue;
        }
        else {
            int ownerId = Integer.parseInt(ownerIdInput);
            companyName = companyName.trim();
            Owner owner = ownerRepository.findOne(ownerId);
            Company company = new Company();
            if (owner == null) {
                returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
                return returnValue;
            } else {
                if (session.getAttribute("ownerSession") == null) {
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
                            if (companyName.isEmpty()) {
                                returnValue = new CompanyReturnValue(company, GeneralConstant.RESULT_CODE_COMPANYNAME_EMPTY);
                                return returnValue;
                            } else {
                                company.setName(companyName);
                                company.setOwnerId(ownerId);
                                companyRepository.save(company);
                                owner.setCompanyId(company.getId());
                                ownerRepository.save(owner);
                                returnValue = new CompanyReturnValue(company, GeneralConstant.RESULT_CODE_VALID);
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
    }
    @RequestMapping(value="/update",method=RequestMethod.POST)
    public CompanyReturnValue updateCompany(@RequestParam("companyId")String companyIdInput,
                                @RequestParam("companyName")String companyName,
                                @RequestParam("ownerId")String ownerIdInput,
                                HttpSession session) {
        CompanyReturnValue returnValue;
        if(companyIdInput.isEmpty()){
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_EMPTY);
            return returnValue;
        }
        else
        if(ownerIdInput.isEmpty()){
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_EMPTY);
            return returnValue;
        }
        else {
            int ownerId = Integer.parseInt(ownerIdInput);
            int companyId = Integer.parseInt(companyIdInput);
            companyName = companyName.trim();
            Owner owner = ownerRepository.findOne(ownerId);
            if (owner == null) {
                returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
                return returnValue;
            } else {
                if (session.getAttribute("ownerSession") == null) {
                    returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                } else {
                    Owner ownerSession = (Owner) session.getAttribute("ownerSession");
                    if (ownerSession.getId().equals(owner.getId())) {
                        Company company = companyRepository.findOne(companyId);
                        if (company == null) {
                            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_EXISTED);
                            return returnValue;
                        }//company không tồn tại
                        else {
                            if (companyName.isEmpty()) {
                                returnValue = new CompanyReturnValue(company, GeneralConstant.RESULT_CODE_COMPANYNAME_EMPTY);
                                return returnValue;
                            } else {
                                company.setName(companyName);
                                company.setOwnerId(ownerId);
                                owner.setCompanyId(companyId);
                                companyRepository.save(company);
                                ownerRepository.save(owner);
                                returnValue = new CompanyReturnValue(company, GeneralConstant.RESULT_CODE_VALID);
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
    }

    @RequestMapping(value="/del",method=RequestMethod.POST)
    public CompanyReturnValue delCompany(@RequestParam("companyId") String companyIdInput,
                             @RequestParam("ownerId")String ownerIdInput,
                             HttpSession session) {
        CompanyReturnValue returnValue;
        if(companyIdInput.isEmpty()){
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_EMPTY);
            return returnValue;
        }
        else
        if(ownerIdInput.isEmpty()){
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_EMPTY);
            return returnValue;
        }
        else {
            int ownerId = Integer.parseInt(ownerIdInput);
            int companyId = Integer.parseInt(companyIdInput);
            Owner owner = ownerRepository.findOne(ownerId);
            if (owner == null) {
                returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
                return returnValue;
            } else {
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
                                                } else {
                                                    returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_TASKCHILDLIST_EMPTY);
                                                    return returnValue;
                                                }
                                                taskRepository.delete(taskList.get(j));
                                            }
                                        } else {
                                            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_TASKLIST_EMPTY);
                                            return returnValue;
                                        }
                                        projectRepository.delete(projectList.get(i));
                                    }
                                } else {
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
    }

    @RequestMapping(value="/listAll",method=RequestMethod.GET)
    public CompanyReturnValue listAllCompany(){
        CompanyReturnValue returnValue;
        List<Company> companyList = (List<Company>) companyRepository.findAll();
        if(companyList==null){
            returnValue = new CompanyReturnValue(GeneralConstant.RESULT_CODE_COMPANYLIST_EMPTY);
            return returnValue;
        }
        else {
            for (Company companyEntry : companyList) {
                viewCompany(companyEntry.getId().toString());
            }
            companyRepository.save(companyList);
            returnValue = new CompanyReturnValue(companyList,GeneralConstant.RESULT_CODE_VALID);
            return returnValue;
        }
    }

}
