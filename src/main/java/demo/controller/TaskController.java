package demo.controller;

import demo.model.Employee;
import demo.model.Project;
import demo.model.Task;
import demo.repository.CompanyRepository;
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
 * Created by Khang on 24/06/2015.
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    TaskRepository taskRepository;



    @RequestMapping(value="/add",method=RequestMethod.POST)
    public String addTask(@RequestParam("taskId")String taskId,
                          @RequestParam("taskName")String taskName){
        Task task = new Task();
        task.setTaskId(taskId);
        task.setTaskName(taskName);
        taskRepository.save(task);

        return "Added";
    }

    @RequestMapping(value="/update",method=RequestMethod.POST)
    public String updateTask(@RequestParam("taskId")String taskId,
                          @RequestParam("taskName")String taskName){
        Task task = taskRepository.findOne(taskId);
        task.setTaskName(taskName);
        taskRepository.save(task);

        return "Update";
    }

    @RequestMapping(value="/listByProjectId",method=RequestMethod.GET)
    public List<Task> listTask(@RequestParam("projectId")String projectId){
        List<Task> listTask = new ArrayList<Task>();
        listTask = taskRepository.listTaskByProjectId(projectId);
        for(Task taskEntry : listTask){
            List<Task> listTaskChild = listTaskChild(taskEntry.getTaskId());
            taskRepository.save(listTaskChild);
        }
        taskRepository.save(listTask);
        return listTask;
    }

    @RequestMapping(value="/listTaskChildByTaskId",method=RequestMethod.GET)
    public List<Task> listTaskChild(@RequestParam("taskId")String taskId){
        List<Task> taskChildList = new ArrayList<Task>();
        taskChildList = taskRepository.listTaskChildByTaskId(taskId);

        return taskChildList;
    }

    @RequestMapping(value="/addTaskToProject",method=RequestMethod.POST)
    public String addTaskToProject(@RequestParam("taskId")String taskId,
                                   @RequestParam("projectId")String projectId){
        Project project = projectRepository.findOne(projectId);
        Task task = taskRepository.findOne(taskId);
        task.setProjectId(projectId);
        taskRepository.save(task);
        project.setListTask(taskRepository.listTaskByProjectId(projectId));
        projectRepository.save(project);
        return "Added";
    }

    @RequestMapping(value="/addTaskChildToTask",method=RequestMethod.POST)
    public String addTaskChildToTask(@RequestParam("taskChildId")String taskChildId,
                                   @RequestParam("taskParentId")String taskParentId){
        Task taskParent = taskRepository.findOne(taskParentId);
        Task taskChild = taskRepository.findOne(taskChildId);
        taskChild.setTaskParentId(taskParentId);
        taskRepository.save(taskChild);
        taskParent.setTaskChild(taskRepository.listTaskChildByTaskId(taskParentId));
        taskRepository.save(taskParent);

        return "Added";
    }

    @RequestMapping(value="/delTaskChild", method=RequestMethod.DELETE)
    public void delTaskChild(@RequestParam("taskChildId")String taskChildId){
        Task taskChild = taskRepository.findOne(taskChildId);
        Task taskParent = taskRepository.findOne(taskChild.getTaskParentId());
        taskRepository.delete(taskChild);
        taskParent.setTaskChild(taskRepository.listTaskChildByTaskId(taskParent.getTaskId()));
        taskRepository.save(taskParent);

    }

    @RequestMapping(value="/delTask", method = RequestMethod.DELETE)
    public void delTask(@RequestParam("taskId")String taskId){
        Task task = taskRepository.findOne(taskId);
        Project project = projectRepository.findOne(task.getProjectId());
        List<Task> taskChildList = taskRepository.listTaskChildByTaskId(taskId);
        for(int i=0;i<taskChildList.size();i++){
            taskRepository.delete(taskChildList.get(i));
            //delTaskChild(taskChildList.get(i).getTaskId());
        }
        taskRepository.delete(task);
        project.setListTask(taskRepository.listTaskByProjectId(project.getProjectId()));
        projectRepository.save(project);
    }
}
