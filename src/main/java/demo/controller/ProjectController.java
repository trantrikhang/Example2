package demo.controller;

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

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public Project addProject(@RequestParam("projectId") String projectId,
                              @RequestParam("projectName") String projectName){
        Project project=new Project();
        project.setProjectName(projectName);
        project.setProjectId(projectId);
        projectRepository.save(project);

        for(int i=0;i<3;i++){
            Task task = new Task();
            task.setTaskName("task " + i);
            task.setProject(project);
            taskRepository.save(task);

            for(int j=0;j<3;j++){
                Task taskChild=new Task();
                taskChild.setTaskName("task child " + j);
                taskChild.setProject(project);
                taskChild.setTaskParent(task);
                taskRepository.save(taskChild);
            }
        }
        return project;

    }
    @RequestMapping(value="/list_all_project",method=RequestMethod.GET)
    public Project listAllProject(@RequestParam("companyId")String companyId){
        List<Project> listProject = new ArrayList<Project>();
        listProject=companyRepository.listProjectByCompanyId(companyId);
        Integer count = listProject.size();
        for(int i=0;i<count;i++){
            return listProject.get(i);
        }
        return listProject.get(0);
    }

    @RequestMapping(value="/add_employee",method=RequestMethod.POST)
    public Project addEmployeeToProject(@RequestParam("projectId")String projectId,
                                 @RequestParam("employee")Employee employee){
        Project project = projectRepository.findOne(projectId);
        if(project.getCompanyId()==employee.getCompany().getCompanyId()){
            project.addEmployeeToProject(employee);
            projectRepository.save(project);
            employee.setProject(project);
            employeeRepository.save(employee);
        }
        return project;
    }

    @RequestMapping(value="/list_employee",method=RequestMethod.POST)
    public Employee listProjectEmployee(@RequestParam("projectId")String projectId){
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList=projectRepository.getListOfEmployeeById(projectId);
        int count = employeeList.size();
        for(int i=0;i<count;i++){
            return employeeList.get(i);
        }
        return employeeList.get(0);
    }
}
