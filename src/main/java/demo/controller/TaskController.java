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

    @RequestMapping(value="/list_task_by_projectId",method=RequestMethod.POST)
    public Task listTask(@RequestParam("project_id")String projectId){
        List<Task> listTask = new ArrayList<Task>();
        listTask = projectRepository.getListOfTaskById(projectId);
        Integer count = listTask.size();
        for(int i=0;i<count;i++){
            return listTask.get(i);
        }
        return listTask.get(0);
    }

    @RequestMapping(value="/list_task_child_by_taskId",method=RequestMethod.POST)
    public Task listTaskChild(@RequestParam("taskId")String taskId){
        List<Task> listTaskChild = new ArrayList<Task>();
        listTaskChild = taskRepository.getListOfTaskChildById(taskId);
        Integer count = listTaskChild.size();
        for(int i=0;i<count;i++){
            return listTaskChild.get(i);
        }
        return listTaskChild.get(0);
    }
}
