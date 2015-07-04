package demo.controller;

import demo.bean.EmployeeReturnValue;
import demo.bean.ProjectReturnValue;
import demo.model.*;
import demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
@RestController
@RequestMapping("/project")
@SessionAttributes({"ownerSession","employeeSession"})
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    TaskController taskController;
    @Autowired
    JpaEmployeeRepository jpaEmployeeRepository;
    @Autowired
    JpaProjectRepository jpaProjectRepository;
    @Autowired
    JpaTaskRepository jpaTaskRepository;

    @RequestMapping(value = "/addProject", method = RequestMethod.POST)
    public ProjectReturnValue addProject(@RequestParam("projectName") String projectName,
                             @RequestParam("companyId") Integer companyId,
                             @RequestParam("projectManagerId")Integer projectManagerId,
                             @RequestParam("ownerId") Integer ownerId,
                             HttpSession session) {
        ProjectReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if(owner==null){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null) {
                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            }
            else {
                Owner ownerSession = (Owner) session.getAttribute("ownerSession");
                if (ownerSession.getId().equals(owner.getId())) {

                    if (employeeRepository.exists(projectManagerId)) {
                        if (companyRepository.exists(companyId)) {
                            Project project = new Project(projectName, companyId, projectManagerId);
                            projectRepository.save(project);
                            Employee projectManager = employeeRepository.findOne(projectManagerId);
                            employeeRepository.save(projectManager);
                            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_VALID);
                            returnValue.setProject(project);
                            returnValue.setProjectList(null);
                            return returnValue;
                        } else {
                            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_NOT_EXIST);
                            return returnValue;
                        }
                    } else {
                        returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTMANAGERID_NOT_EXIST);
                        return returnValue;
                    }
                }
                else {
                    returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/updateProject",method=RequestMethod.POST)
    public ProjectReturnValue updateProject(@RequestParam("projectId") Integer projectId,
                                @RequestParam("projectName") String projectName,
                                @RequestParam("companyId")Integer companyId,
                                @RequestParam("projectManagerId")Integer projectManagerId,
                                @RequestParam("ownerId") Integer ownerId,
                                HttpSession session) {
        ProjectReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if(owner==null){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null){
                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            }
            else {
                Owner ownerSession = (Owner) session.getAttribute("ownerSession");
                if (ownerSession.getId().equals(owner.getId())) {
                    Project project = projectRepository.findOne(projectId);
                    if(project==null){
                        returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                        return returnValue;
                    }
                    else {
                        project.setName(projectName);
                        if(employeeRepository.exists(projectManagerId)) {
                            project.setProjectManagerId(projectManagerId);
                            if (companyRepository.exists(companyId)) {
                                project.setCompanyId(companyId);
                                projectRepository.save(project);
                                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_VALID);
                                returnValue.setProject(project);
                                returnValue.setProjectList(null);
                                return returnValue;
                            }
                            else{
                                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_NOT_EXIST);
                                return returnValue;
                            }
                        }
                        else{
                            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTMANAGERID_NOT_EXIST);
                            return returnValue;
                        }
                    }
                }
                else{
                    returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/addEmployeeToProject",method=RequestMethod.POST)
    public EmployeeReturnValue addEmployeeToProject(@RequestParam("projectId")Integer projectId,
                                       @RequestParam("employeeId")Integer employeeId,
                                       @RequestParam("projectManagerId") Integer projectManagerId,
                                       HttpSession session) {
        EmployeeReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
            return returnValue;
        }
        else {
            Employee projectManager = employeeRepository.findOne(projectManagerId);
            if (projectManager == null){
                returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_PROJECTMANAGERID_NOT_EXIST);
                return returnValue;
            }
            else {
                Company company = companyRepository.findOne(projectManager.getCompanyId());
                if (company.getId().equals(projectManager.getCompanyId())) {
                    if (session.getAttribute("employeeSession") == null){
                        returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                        return returnValue;
                    }
                    else {
                        Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                        if (employeeSession.getId().equals(projectManagerId)) {
                            if (projectRepository.exists(projectId)) {
                                if (employee.getProjectId() == null) {
                                    employee.setProjectId(projectId);
                                    employeeRepository.save(employee);
                                    returnValue = new EmployeeReturnValue( GeneralConstant.RESULT_CODE_VALID);
                                    returnValue.setEmployee(employee);
                                    returnValue.setEmployeeList(null);
                                    return returnValue;
                                }
                                else{
                                    returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEES_PROJECTID_EXISTED);
                                    return returnValue;
                                }
                            }
                            else{
                                returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                                return returnValue;
                            }
                        }
                        else{
                            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_MANAGERID_NOT_LOGIN);
                            return returnValue;
                        }
                    }
                }
                else{
                    returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_MANAGERID_NOT_BELONG_TO_COMPANY);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/del",method=RequestMethod.POST)
    public ProjectReturnValue delProject(@RequestParam("projectId")Integer projectId,
                             @RequestParam("employeeId")Integer employeeId,
                           HttpSession session) {
        ProjectReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null){
                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            }
            else {
                Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                if (employeeSession.getId().equals(employee.getId())) {
                    Project project = projectRepository.findOne(projectId);
                    if(project==null){
                        returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                        return returnValue;
                    }
                    else {
                        List<Task> taskList = jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectId);
                        if (taskList == null){
                            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_TASKLIST_EMPTY);
                            return returnValue;
                        }
                        else {
                            for (int i = 0; i < taskList.size(); i++) {
                                taskController.delTask(taskList.get(i).getId(), employeeId, session);
                            }
                        }
                        List<Employee> employeeList = jpaEmployeeRepository.findByProjectId(projectId);
                        if(employeeList==null){
                            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEELIST_EMPTY);
                            return returnValue;
                        }
                        else {
                            for (int i = 0; i < employeeList.size(); i++) {
                                employeeList.get(i).setProjectId(null);
                                employeeRepository.save(employeeList.get(i));
                            }
                        }
                        projectRepository.delete(project);
                        returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_VALID);
                        returnValue.setProject(null);
                        returnValue.setProjectList(null);
                        return returnValue;
                    }
                }
                else {
                    returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_MANAGERID_NOT_BELONG_TO_COMPANY);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/view",method=RequestMethod.GET)
    public ProjectReturnValue viewProject(@RequestParam("projectId") Integer projectId){
        ProjectReturnValue returnValue;
        Project project = projectRepository.findOne(projectId);
        if(project==null){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
            return returnValue;
        }
        else {
            project.setListEmployeeProject(jpaEmployeeRepository.findByProjectId(projectId));
            project.setListTask(jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectId));
            for(Task taskEntry : project.getListTask()){
                taskEntry.setTaskChild(jpaTaskRepository.findByTaskParentId(taskEntry.getId()));
            }
            projectRepository.save(project);
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_VALID);
            returnValue.setProject(project);
            returnValue.setProjectList(null);
            return returnValue;
        }
    }

    @RequestMapping(value="/listByCompanyId",method=RequestMethod.GET)
    public  ProjectReturnValue listByCompanyId(@RequestParam("companyId")Integer companyId){
        ProjectReturnValue returnValue;
        List<Project> projectList = jpaProjectRepository.findByCompanyId(companyId);
        if(projectList==null){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_NOT_EXIST);
            return returnValue;
        }
        else {
            for (Project projectEntry : projectList) {
                projectEntry.setListEmployeeProject(jpaEmployeeRepository.findByProjectId(projectEntry.getId()));
                projectEntry.setListTask(jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectEntry.getId()));
                for (Task taskEntry : projectEntry.getListTask()) {
                    taskEntry.setTaskChild(jpaTaskRepository.findByTaskParentId(taskEntry.getId()));
                }
                viewProject(projectEntry.getId());
                projectRepository.save(projectEntry);
            }
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_VALID);
            returnValue.setProjectList(projectList);
            returnValue.setProject(null);
            return returnValue;
        }
    }

    @RequestMapping(value="/listAll",method = RequestMethod.GET)
    public  ProjectReturnValue listAll() {
        ProjectReturnValue returnValue;
        List<Project> projectList = (List<Project>) projectRepository.findAll();
        if (projectList == null) {
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_NOT_EXIST);
            return returnValue;
        } else {
            for (Project projectEntry : projectList) {
                viewProject(projectEntry.getId());
            }
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_VALID);
            returnValue.setProjectList(projectList);
            returnValue.setProject(null);
            return returnValue;
        }
    }
}
