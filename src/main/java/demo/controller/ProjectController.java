package demo.controller;

import demo.model.Company;
import demo.model.Employee;
import demo.model.Project;
import demo.model.Task;
import demo.repository.CompanyRepository;
import demo.repository.EmployeeRepository;
import demo.repository.ProjectRepository;
//import demo.repository.TaskRepository;
import demo.repository.TaskRepository;
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
                                 @RequestParam("employeeId")String employeeId){
        Project project = projectRepository.findOne(projectId);
        Employee employee = employeeRepository.findOne(employeeId);
        if(projectRepository.exists(projectId)) {
            if (employeeRepository.exists(employeeId)) {
                if (employee.getProjectId() == null) {
                    employee.setProjectId(projectId);
                    employeeRepository.save(employee);
                } else return "Employee's projectId have already existed";
            } else return "Wrong employeeId";
        } else return "Wrong projectId";
        return "Added";
    }
}
