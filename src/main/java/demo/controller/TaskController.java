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

    @RequestMapping(value="/listByProjectId",method=RequestMethod.GET)
    public List<Task> listTask(@RequestParam("projectId")String projectId){
        List<Task> listTask = new ArrayList<Task>();
        listTask = taskRepository.listTaskByProjectId(projectId);

        return listTask;
    }

    @RequestMapping(value="/listTaskChildByTaskId",method=RequestMethod.GET)
    public List<Task> listTaskChild(@RequestParam("taskId")String taskId){
        List<Task> taskChildList = new ArrayList<Task>();
        taskChildList = taskRepository.listTaskChildByTaskId(taskId);

        return taskChildList;
    }

}
