package demo.controller;

import demo.model.Employee;
import demo.model.Project;
import demo.model.Task;
import demo.repository.ProjectRepository;
import demo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Khang on 18/06/2015.
 */
@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskRepository taskRepository;

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public Project addProject(@RequestParam("project_id") String project_id,
                              @RequestParam("name") String name,
                              @RequestParam("manager") Employee manager){
        Project project=new Project();
        project.setName(name);
        project.setProject_id(project_id);
        project.setManager_id(manager);
        projectRepository.save(project);

        for(int i=0;i<3;i++){
            Task task = new Task();
            task.setName("task "+i);
            task.setProject(project);
            taskRepository.save(task);

            for(int j=0;j<3;j++){
                Task task_child=new Task();
                task_child.setName("task child "+j);
                task_child.setProject(project);
                task_child.setTaskParent(task);
                taskRepository.save(task_child);
            }
        }
        return project;
    }
}
