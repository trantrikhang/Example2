package demo.controller;

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
    public ProjectReturnValue addProject(@RequestParam("projectId") String projectId,
                             @RequestParam("projectName") String projectName,
                             @RequestParam("companyId") String companyId,
                             @RequestParam("projectManagerId")String projectManagerId,
                             @RequestParam("ownerId") String ownerId,
                             HttpSession session) {
        HashMap errorList = new HashMap();
        ProjectReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if(owner==null){
            errorList.put(-200, "ownerId not exist");
            returnValue = new ProjectReturnValue((Project)null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null) {
                errorList.put(-100, "must login first");
                returnValue = new ProjectReturnValue((Project)null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("session").equals(owner.getId())) {
                    if (projectRepository.exists(projectId)) {
                        errorList.put(-401, "projectId existed");
                        returnValue = new ProjectReturnValue((Project)null, errorList);
                        return returnValue;
                    }
                    else {
                        if (employeeRepository.exists(projectManagerId)) {
                            if (companyRepository.exists(companyId)) {
                                Project project = new Project(projectId, projectName, companyId, projectManagerId);
                                projectRepository.save(project);
                                Employee projectManager = employeeRepository.findOne(projectManagerId);
                                projectManager.setProjectId(projectId);
                                employeeRepository.save(projectManager);
                                errorList.put(2, "added successful");
                                returnValue = new ProjectReturnValue(project, errorList);
                                return returnValue;
                            }
                            else {
                                errorList.put(-300, "companyId not exist");
                                returnValue = new ProjectReturnValue((Project)null, errorList);
                                return returnValue;
                            }
                        }
                        else {
                            errorList.put(-402, "projectManagerId not exist");
                            returnValue = new ProjectReturnValue((Project)null, errorList);
                            return returnValue;
                        }
                    }
                }
                else {
                    errorList.put(-202, "ownerId not login");
                    returnValue = new ProjectReturnValue((Project)null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/updateProject",method=RequestMethod.POST)
    public ProjectReturnValue updateProject(@RequestParam("projectId") String projectId,
                                @RequestParam("projectName") String projectName,
                                @RequestParam("companyId")String companyId,
                                @RequestParam("projectManagerId")String projectManagerId,
                                @RequestParam("ownerId") String ownerId,
                                HttpSession session) {
        HashMap errorList = new HashMap();
        ProjectReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if(owner==null){
            errorList.put(-200, "ownerId not exist");
            returnValue = new ProjectReturnValue((Project)null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null){
                errorList.put(-100, "must login first");
                returnValue = new ProjectReturnValue((Project)null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("ownerSession").equals(owner.getId())) {
                    Project project = projectRepository.findOne(projectId);
                    if(project==null){
                        errorList.put(-403, "projectId not exist");
                        returnValue = new ProjectReturnValue((Project)null, errorList);
                        return returnValue;
                    }
                    else {
                        project.setName(projectName);
                        if(employeeRepository.exists(projectManagerId)) {
                            project.setProjectManagerId(projectManagerId);
                            if (companyRepository.exists(companyId)) {
                                project.setCompanyId(companyId);
                                projectRepository.save(project);
                                errorList.put(3, "updated successful");
                                returnValue = new ProjectReturnValue(project, errorList);
                                return returnValue;
                            }
                            else{
                                errorList.put(-300, "companyId not exist");
                                returnValue = new ProjectReturnValue((Project)null, errorList);
                                return returnValue;
                            }
                        }
                        else{
                            errorList.put(-402, "projectManagerId not exist");
                            returnValue = new ProjectReturnValue((Project)null, errorList);
                            return returnValue;
                        }
                    }
                }
                else{
                    errorList.put(-202, "ownerId not login");
                    returnValue = new ProjectReturnValue((Project)null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/addEmployeeToProject",method=RequestMethod.POST)
    public EmployeeReturnValue addEmployeeToProject(@RequestParam("projectId")String projectId,
                                       @RequestParam("employeeId")String employeeId,
                                       @RequestParam("projectManagerId") String projectManagerId,
                                       HttpSession session) {
        HashMap errorList = new HashMap();
        EmployeeReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null){
            errorList.put(-601, "employeeId not exist");
            returnValue = new EmployeeReturnValue((Employee)null, errorList);
            return returnValue;
        }
        else {
            Employee projectManager = employeeRepository.findOne(projectManagerId);
            if (projectManager == null){
                errorList.put(-402, "projectManagerId not exist");
                returnValue = new EmployeeReturnValue((Employee)null, errorList);
                return returnValue;
            }
            else {
                Company company = companyRepository.findOne(projectManager.getCompanyId());
                if (company.getCompanyId().equals(projectManager.getCompanyId())) {
                    if (session.getAttribute("employeeSession") == null){
                        errorList.put(-100, "must login first");
                        returnValue = new EmployeeReturnValue((Employee)null, errorList);
                        return returnValue;
                    }
                    else {
                        if (session.getAttribute("employeeSession").equals(projectManagerId)) {
                            if (projectRepository.exists(projectId)) {
                                if (employee.getProjectId() == null) {
                                    employee.setProjectId(projectId);
                                    employeeRepository.save(employee);
                                    errorList.put(2, "added successful");
                                    returnValue = new EmployeeReturnValue(employee, errorList);
                                    return returnValue;
                                }
                                else{
                                    errorList.put(-603, "employee's projectId existed");
                                    returnValue = new EmployeeReturnValue((Employee)null, errorList);
                                    return returnValue;
                                }
                            }
                            else{
                                errorList.put(-403, "projectId not exist");
                                returnValue = new EmployeeReturnValue((Employee)null, errorList);
                                return returnValue;
                            }
                        }
                        else{
                            errorList.put(-604, "managerId not login");
                            returnValue = new EmployeeReturnValue((Employee)null, errorList);
                            return returnValue;
                        }
                    }
                }
                else{
                    errorList.put(-605, "managerId not belong to project's company");
                    returnValue = new EmployeeReturnValue((Employee)null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/del",method=RequestMethod.DELETE)
    public ProjectReturnValue delProject(@RequestParam("projectId")String projectId,
                             @RequestParam("employeeId")String employeeId,
                           HttpSession session) {
        HashMap errorList = new HashMap();
        ProjectReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            errorList.put(-601, "employeeId not exist");
            returnValue = new ProjectReturnValue((Project)null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null){
                errorList.put(-100, "must login first");
                returnValue = new ProjectReturnValue((Project)null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("employeeSession").equals(employee.getId())) {
                    Project project = projectRepository.findOne(projectId);
                    if(project==null){
                        errorList.put(-403, "projectId not exist");
                        returnValue = new ProjectReturnValue((Project)null, errorList);
                        return returnValue;
                    }
                    else {
                        List<Task> taskList = jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectId);
                        if (taskList == null){
                            errorList.put(-501, "taskList is empty");
                        }
                        else {
                            for (int i = 0; i < taskList.size(); i++) {
                                taskController.delTask(taskList.get(i).getId(), employeeId, session);
                            }
                        }
                        List<Employee> employeeList = jpaEmployeeRepository.findByProjectId(projectId);
                        if(employeeList==null){
                            errorList.put(-600, "employeeList is empty");
                        }
                        else {
                            for (int i = 0; i < employeeList.size(); i++) {
                                employeeList.get(i).setId(null);
                                employeeRepository.save(employeeList.get(i));
                            }
                        }
                        projectRepository.delete(project);
                        errorList.put(4, "deleted successful");
                        returnValue = new ProjectReturnValue(project, errorList);
                        return returnValue;
                    }
                }
                else {
                    errorList.put(-605, "managerId not belong to project's company");
                    returnValue = new ProjectReturnValue((Project)null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/view",method=RequestMethod.GET)
    public ProjectReturnValue viewProject(@RequestParam("projectId") String projectId){
        HashMap errorList = new HashMap();
        ProjectReturnValue returnValue;
        Project project = projectRepository.findOne(projectId);
        if(project==null){
            errorList.put(-403, "projectId not exist");
            returnValue = new ProjectReturnValue((Project)null, errorList);
            return returnValue;
        }
        else {
            project.setListEmployeeProject(jpaEmployeeRepository.findByProjectId(projectId));
            project.setListTask(jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectId));
            for(Task taskEntry : project.getListTask()){
                taskEntry.setTaskChild(jpaTaskRepository.findByTaskParentId(taskEntry.getId()));
            }
            projectRepository.save(project);
            errorList.put(1, "list successful");
            returnValue = new ProjectReturnValue(project, errorList);
            return returnValue;
        }
    }

//    @RequestMapping(value="/listByCompanyId",method=RequestMethod.GET)
//    public  ProjectReturnValue listByCompanyId(@RequestParam("companyId")String companyId){
//        HashMap errorList = new HashMap();
//        ProjectReturnValue returnValue;
//        List<Project> projectList = jpaProjectRepository.findByCompanyId(companyId);
//        if(projectList==null){
//            errorList.put(-300, "companyId not exist");
//            returnValue = new ProjectReturnValue((Project)null, errorList);
//            return returnValue;
//        }
//        else {
//            for (Project projectEntry : projectList) {
//                //projectEntry=viewProject(projectEntry.getId());
////            projectEntry.setListEmployeeProject(jpaEmployeeRepository.findByProjectId(projectEntry.getId()));
////            projectEntry.setListTask(jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectEntry.getId()));
////            for(Task taskEntry : projectEntry.getListTask()){
////                taskEntry.setTaskChild(jpaTaskRepository.findByTaskParentId(taskEntry.getId()));
////            }
//                viewProject(projectEntry.getId());
//                projectRepository.save(projectEntry);
//            }
//            errorList.put(1, "list successful");
//            returnValue = new ProjectReturnValue(projectList, errorList);
//            return returnValue;
//        }
//    }

    @RequestMapping(value="/listAll",method = RequestMethod.GET)
    public  Iterable<Project> listAll(){
        Iterable<Project> projectList = projectRepository.findAll();
        for(Project projectEntry : projectList){
            viewProject(projectEntry.getId());
        }
        return projectList;
    }
}
