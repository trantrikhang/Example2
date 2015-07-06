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
                             @RequestParam("companyId") String companyIdInput,
                             @RequestParam("projectManagerId")String projectManagerIdInput,
                             @RequestParam("ownerId") String ownerIdInput,
                             HttpSession session) {
        ProjectReturnValue returnValue;
        if(companyIdInput.isEmpty()){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_EMPTY);
            return returnValue;
        }
        else
        if(projectManagerIdInput.isEmpty()){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else
        if(ownerIdInput.isEmpty()){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_OWNERID_EMPTY);
            return returnValue;
        }
        else {
            int projectManagerId = Integer.parseInt(projectManagerIdInput);
            int ownerId = Integer.parseInt(ownerIdInput);
            int companyId = Integer.parseInt(companyIdInput);
            projectName = projectName.trim();
            Owner owner = ownerRepository.findOne(ownerId);
            if (owner == null) {
                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
                return returnValue;
            } else {
                if (session.getAttribute("ownerSession") == null) {
                    returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                } else {
                    Owner ownerSession = (Owner) session.getAttribute("ownerSession");
                    if (ownerSession.getId().equals(owner.getId())) {

                        if (employeeRepository.exists(projectManagerId)) {
                            if (companyRepository.exists(companyId)) {
                                if (projectName.isEmpty()) {
                                    returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTNAME_EMPTY);
                                    return returnValue;
                                } else {
                                    Project project = new Project(projectName, companyId, projectManagerId);
                                    projectRepository.save(project);
                                    Employee projectManager = employeeRepository.findOne(projectManagerId);
                                    employeeRepository.save(projectManager);
                                    returnValue = new ProjectReturnValue(project, GeneralConstant.RESULT_CODE_VALID);
                                    return returnValue;
                                }
                            } else {
                                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_NOT_EXIST);
                                return returnValue;
                            }
                        } else {
                            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTMANAGERID_NOT_EXIST);
                            return returnValue;
                        }
                    } else {
                        returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_LOGIN);
                        return returnValue;
                    }
                }
            }
        }
    }

    @RequestMapping(value="/updateProject",method=RequestMethod.POST)
    public ProjectReturnValue updateProject(@RequestParam("projectId") String projectIdInput,
                                @RequestParam("projectName") String projectName,
                                @RequestParam("companyId")String companyIdInput,
                                @RequestParam("projectManagerId")String projectManagerIdInput,
                                @RequestParam("ownerId") String ownerIdInput,
                                HttpSession session) {
        ProjectReturnValue returnValue;
        if(companyIdInput.isEmpty()){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_EMPTY);
            return returnValue;
        }
        else
        if(projectManagerIdInput.isEmpty()){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else
        if(ownerIdInput.isEmpty()){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_OWNERID_EMPTY);
            return returnValue;
        }
        else
        if(projectIdInput.isEmpty()){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_EMPTY);
            return returnValue;
        }
        else {
            int projectManagerId = Integer.parseInt(projectManagerIdInput);
            int ownerId = Integer.parseInt(ownerIdInput);
            int companyId = Integer.parseInt(companyIdInput);
            int projectId = Integer.parseInt(projectIdInput);
            projectName = projectName.trim();
            Owner owner = ownerRepository.findOne(ownerId);
            if (owner == null) {
                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
                return returnValue;
            } else {
                if (session.getAttribute("ownerSession") == null) {
                    returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                } else {
                    Owner ownerSession = (Owner) session.getAttribute("ownerSession");
                    if (ownerSession.getId().equals(owner.getId())) {
                        Project project = projectRepository.findOne(projectId);
                        if (project == null) {
                            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                            return returnValue;
                        } else {
                            project.setName(projectName);
                            if (employeeRepository.exists(projectManagerId)) {
                                project.setProjectManagerId(projectManagerId);
                                if (companyRepository.exists(companyId)) {
                                    if (projectName.isEmpty()) {
                                        returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTNAME_EMPTY);
                                        return returnValue;
                                    } else {
                                        project.setCompanyId(companyId);
                                        projectRepository.save(project);
                                        returnValue = new ProjectReturnValue(project, GeneralConstant.RESULT_CODE_VALID);
                                        return returnValue;
                                    }
                                } else {
                                    returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_NOT_EXIST);
                                    return returnValue;
                                }
                            } else {
                                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTMANAGERID_NOT_EXIST);
                                return returnValue;
                            }
                        }
                    } else {
                        returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_LOGIN);
                        return returnValue;
                    }
                }
            }
        }
    }

    @RequestMapping(value="/addEmployeeToProject",method=RequestMethod.POST)
    public EmployeeReturnValue addEmployeeToProject(@RequestParam("projectId")String projectIdInput,
                                       @RequestParam("employeeId")String employeeIdInput,
                                       @RequestParam("projectManagerId") String projectManagerIdInput,
                                       HttpSession session) {
        EmployeeReturnValue returnValue;
        if(projectManagerIdInput.isEmpty()){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else
        if(employeeIdInput.isEmpty()){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else
        if(projectIdInput.isEmpty()){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_EMPTY);
            return returnValue;
        }
        else {
            int projectManagerId = Integer.parseInt(projectManagerIdInput);
            int employeeId = Integer.parseInt(employeeIdInput);
            int projectId = Integer.parseInt(projectIdInput);
            Employee employee = employeeRepository.findOne(employeeId);
            if (employee == null) {
                returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
                return returnValue;
            } else {
                Employee projectManager = employeeRepository.findOne(projectManagerId);
                if (projectManager == null) {
                    returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_PROJECTMANAGERID_NOT_EXIST);
                    return returnValue;
                } else {
                    Company company = companyRepository.findOne(projectManager.getCompanyId());
                    if (company.getId().equals(projectManager.getCompanyId())) {
                        if (session.getAttribute("employeeSession") == null) {
                            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                            return returnValue;
                        } else {
                            Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                            if (employeeSession.getId().equals(projectManagerId)) {
                                if (projectRepository.exists(projectId)) {
                                    if (employee.getProjectId() == null) {
                                        employee.setProjectId(projectId);
                                        employeeRepository.save(employee);
                                        returnValue = new EmployeeReturnValue(employee, GeneralConstant.RESULT_CODE_VALID);
                                        return returnValue;
                                    } else {
                                        returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEES_PROJECTID_EXISTED);
                                        return returnValue;
                                    }
                                } else {
                                    returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                                    return returnValue;
                                }
                            } else {
                                returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_MANAGERID_NOT_LOGIN);
                                return returnValue;
                            }
                        }
                    } else {
                        returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_MANAGERID_NOT_BELONG_TO_COMPANY);
                        return returnValue;
                    }
                }
            }
        }
    }

    @RequestMapping(value="/del",method=RequestMethod.POST)
    public ProjectReturnValue delProject(@RequestParam("projectId")String projectIdInput,
                             @RequestParam("projectManagerId")String projectManagerIdInput,
                           HttpSession session) {
        ProjectReturnValue returnValue;
        if(projectManagerIdInput.isEmpty()){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else
        if(projectIdInput.isEmpty()){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_EMPTY);
            return returnValue;
        }
        else {
            int projectManagerId = Integer.parseInt(projectManagerIdInput);
            int projectId = Integer.parseInt(projectIdInput);
            Employee employee = employeeRepository.findOne(projectManagerId);
            if (employee == null) {
                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
                return returnValue;
            } else {
                if (session.getAttribute("employeeSession") == null) {
                    returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                } else {
                    Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                    if (employeeSession.getId().equals(employee.getId())) {
                        Project project = projectRepository.findOne(projectId);
                        if (project == null) {
                            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                            return returnValue;
                        } else {
                            List<Task> taskList = jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectId);
                            if (taskList == null) {
                                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_TASKLIST_EMPTY);
                                return returnValue;
                            } else {
                                for (int i = 0; i < taskList.size(); i++) {
                                    taskController.delTask(taskList.get(i).getId().toString(), projectManagerIdInput, session);
                                }
                            }
                            List<Employee> employeeList = jpaEmployeeRepository.findByProjectId(projectId);
                            if (employeeList == null) {
                                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEELIST_EMPTY);
                                return returnValue;
                            } else {
                                for (int i = 0; i < employeeList.size(); i++) {
                                    employeeList.get(i).setProjectId(null);
                                    employeeRepository.save(employeeList.get(i));
                                }
                            }
                            projectRepository.delete(project);
                            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_VALID);
                            return returnValue;
                        }
                    } else {
                        returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_MANAGERID_NOT_BELONG_TO_COMPANY);
                        return returnValue;
                    }
                }
            }
        }
    }

    @RequestMapping(value="/view",method=RequestMethod.GET)
    public ProjectReturnValue viewProject(@RequestParam("projectId") String projectIdInput){
        ProjectReturnValue returnValue;
        if(projectIdInput.isEmpty()){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_EMPTY);
            return returnValue;
        }
        else {
            int projectId = Integer.parseInt(projectIdInput);
            Project project = projectRepository.findOne(projectId);
            if (project == null) {
                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                return returnValue;
            } else {
                project.setListEmployeeProject(jpaEmployeeRepository.findByProjectId(projectId));
                project.setListTask(jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectId));
                for (Task taskEntry : project.getListTask()) {
                    taskEntry.setTaskChild(jpaTaskRepository.findByTaskParentId(taskEntry.getId()));
                }
                projectRepository.save(project);
                returnValue = new ProjectReturnValue(project, GeneralConstant.RESULT_CODE_VALID);
                return returnValue;
            }
        }
    }

    @RequestMapping(value="/listByCompanyId",method=RequestMethod.GET)
    public  ProjectReturnValue listByCompanyId(@RequestParam("companyId")String companyIdInput){
        ProjectReturnValue returnValue;
        if(companyIdInput.isEmpty()){
            returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_EMPTY);
            return returnValue;
        }
        else {
            int companyId = Integer.parseInt(companyIdInput);
            List<Project> projectList = jpaProjectRepository.findByCompanyId(companyId);
            if (projectList == null) {
                returnValue = new ProjectReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_NOT_EXIST);
                return returnValue;
            } else {
                for (Project projectEntry : projectList) {
                    projectEntry.setListEmployeeProject(jpaEmployeeRepository.findByProjectId(projectEntry.getId()));
                    projectEntry.setListTask(jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectEntry.getId()));
                    for (Task taskEntry : projectEntry.getListTask()) {
                        taskEntry.setTaskChild(jpaTaskRepository.findByTaskParentId(taskEntry.getId()));
                    }
                    viewProject(projectEntry.getId().toString());
                    projectRepository.save(projectEntry);
                }
                returnValue = new ProjectReturnValue(projectList, GeneralConstant.RESULT_CODE_VALID);
                return returnValue;
            }
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
                viewProject(projectEntry.getId().toString());
            }
            returnValue = new ProjectReturnValue(projectList,GeneralConstant.RESULT_CODE_VALID);
            return returnValue;
        }
    }
}
