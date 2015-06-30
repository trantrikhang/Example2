package demo.controller;

import demo.model.*;
import demo.repository.*;
import demo.controller.*;
//import demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
@RestController
@RequestMapping("/project")
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

    @RequestMapping(value="/login", method=RequestMethod.POST)
    public Owner getOwner(@RequestParam("ownerId")String ownerId,
                          @RequestParam("ownerPassword")String ownerPassword){
        Owner owner = new Owner();
        owner=ownerRepository.getOwner(ownerId,ownerPassword);
        return owner;
    }

    @RequestMapping(value="/addProject",method=RequestMethod.POST)
    public String addProject(@RequestParam("projectId") String projectId,
                              @RequestParam("projectName") String projectName,
                              @RequestParam("companyId")String companyId) {
        Project project=new Project();
        project.setProjectName(projectName);
        project.setProjectId(projectId);
        if(companyRepository.exists(companyId)) project.setCompanyId(companyId);
        else return "Wrong companyId";

        projectRepository.save(project);

        return "Added";

    }

    @RequestMapping(value="/updateProject",method=RequestMethod.POST)
    public String updateProject(@RequestParam("projectId") String projectId,
                             @RequestParam("projectName") String projectName,
                             @RequestParam("companyId")String companyId) {
        Project project=projectRepository.findOne(projectId);
        project.setProjectName(projectName);
        if(companyRepository.exists(companyId)) project.setCompanyId(companyId);
        else return "Wrong companyId";

        projectRepository.save(project);

        return "Updated";

    }

    @RequestMapping(value="/view",method=RequestMethod.GET)
    public Project viewProject(@RequestParam("projectId") String projectId){
        Project project = projectRepository.findOne(projectId);
        project.setListEmployeeProject(employeeRepository.listEmployeeByProjectId(projectId));
        project.setListTask(taskRepository.listTaskByProjectId(projectId));
        List<Task> taskList = new ArrayList<Task>();
        taskList=taskRepository.listTaskByProjectId(projectId);
        for(Task taskEntry : taskList){
            taskEntry.setTaskChild(taskRepository.listTaskChildByTaskId(taskEntry.getTaskParentId()));
            taskRepository.save(taskEntry);
        }
        projectRepository.save(project);
        return project;
    }
    @RequestMapping(value="/listByCompanyId",method=RequestMethod.GET)
    public  List<Project> listByCompanyId(@RequestParam("companyId")String companyId){
        List<Project> projectList = new ArrayList<Project>();
        projectList=projectRepository.listProjectByCompanyId(companyId);
        for(Project projectEntry : projectList){
            projectEntry.setListEmployeeProject(employeeRepository.listEmployeeByProjectId(projectEntry.getProjectId()));
            projectRepository.save(projectEntry);
        }
        return projectList;
    }

    @RequestMapping(value="/listAll",method = RequestMethod.GET)
    public  List<Project> listAll(){
        List<Project> projectList = new ArrayList<Project>();
        projectList = projectRepository.listAllProject();
        for(Project projectEntry : projectList){
            viewProject(projectEntry.getProjectId());
        }
        return projectList;
    }

    @RequestMapping(value="/addEmployeeToProject",method=RequestMethod.POST)
    public String addEmployeeToProject(@RequestParam("projectId")String projectId,
                                       @RequestParam("employeeId")String employeeId,
                                       @RequestParam("ownerId") String ownerId,
                                       @RequestParam("ownerPassword")String ownerPassword){
        Owner owner = getOwner(ownerId,ownerPassword);
        if(owner==null) return "Not be an owner. Cannot add an employee";
        else {
            Project project = projectRepository.findOne(projectId);
            if (owner.getCompanyId() == project.getCompanyId()) {
                Employee employee = employeeRepository.findOne(employeeId);
                if (projectRepository.exists(projectId)) {
                    if (employeeRepository.exists(employeeId)) {
                        if (employee.getProjectId() == null) {
                            employee.setProjectId(projectId);
                            employeeRepository.save(employee);
                        } else return "Employee's projectId have already existed";
                    } else return "Wrong employeeId";
                } else return "Wrong projectId";
            } else return "Wrong Owner info";
        }
        return "Added";
    }

    @RequestMapping(value="/del",method=RequestMethod.DELETE)
    public void delProject(@RequestParam("projectId")String projectId){
        Project project = projectRepository.findOne(projectId);
        List<Task> taskList = taskRepository.listTaskByProjectId(projectId);
        for(int i=0;i<taskList.size();i++){
            taskController.delTask(taskList.get(i).getTaskId());
        }
        Company company = companyRepository.findOne(project.getCompanyId());
        company.setProjectList(projectRepository.listProjectByCompanyId(company.getCompanyId()));
        companyRepository.save(company);

        Employee employee = employeeRepository.findEmployeeByProjectId(projectId);
        employee.setProjectId(null);
        employeeRepository.save(employee);

        projectRepository.delete(project);

    }
}
