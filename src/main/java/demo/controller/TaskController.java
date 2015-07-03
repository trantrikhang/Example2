package demo.controller;

import demo.model.*;
import demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
/**
 * Created by Khang on 24/06/2015.
 */
@RestController
@RequestMapping("/task")
@SessionAttributes({"ownerSession","employeeSession"})
public class TaskController {
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
    JpaTaskRepository jpaTaskRepository;

    @RequestMapping(value="/addTaskToProject",method=RequestMethod.POST)
    public TaskReturnValue addTaskToProject(@RequestParam("taskId")String taskId,
                                   @RequestParam("taskName")String taskName,
                                   @RequestParam("projectId")String projectId,
                                   @RequestParam("employeeId")String employeeId,
                                   HttpSession session) {
        HashMap errorList = new HashMap();
        TaskReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            errorList.put(-601, "employeeId not exist");
            returnValue = new TaskReturnValue((Task)null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null) {
                errorList.put(-100, "must login first");
                returnValue = new TaskReturnValue((Task)null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("employeeSession").equals(employee.getId())) {
                    Project project = projectRepository.findOne(projectId);
                    if(project==null){
                        errorList.put(-403, "projectId not exist");
                        returnValue = new TaskReturnValue((Task)null, errorList);
                        return returnValue;
                    }
                    else {
                        if(taskRepository.exists(taskId)){
                            errorList.put(-502, "taskId existed");
                            returnValue = new TaskReturnValue((Task)null, errorList);
                            return returnValue;
                        }
                        else {
                            if(projectRepository.exists(projectId)) {
                                Task task = new Task(taskId,taskName,projectId);
                                taskRepository.save(task);
                                project.setListTask(jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectId));
                                projectRepository.save(project);
                                errorList.put(2, "added successful");
                                returnValue = new TaskReturnValue((Task)task, errorList);
                                return returnValue;
                            }
                            else {
                                errorList.put(-403, "projectId not exist");
                                returnValue = new TaskReturnValue((Task)null, errorList);
                                return returnValue;
                            }
                        }
                    }
                }
                else {
                    errorList.put(-605, "employeeId not login");
                    returnValue = new TaskReturnValue((Task)null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/addTaskChildToTask",method=RequestMethod.POST)
    public TaskReturnValue addTaskChildToTask(@RequestParam("taskChildId")String taskChildId,
                                     @RequestParam("taskChildName")String taskChildName,
                                     @RequestParam("projectId")String projectId,
                                     @RequestParam("taskParentId")String taskParentId,
                                     @RequestParam("employeeId")String employeeId,
                                     HttpSession session) {
        HashMap errorList = new HashMap();
        TaskReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            errorList.put(-601, "employeeId not exist");
            returnValue = new TaskReturnValue((Task)null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null){
                errorList.put(-100, "must login first");
                returnValue = new TaskReturnValue((Task)null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("employeeSession").equals(employee.getId())) {
                    Task taskParent = taskRepository.findOne(taskParentId);
                    if(taskParent==null){
                        errorList.put(-503, "taskParentId not exist");
                        returnValue = new TaskReturnValue((Task)null, errorList);
                        return returnValue;
                    }
                    else {
                        if(taskRepository.exists(taskChildId)){
                            errorList.put(-504, "taskChildId existed");
                            returnValue = new TaskReturnValue((Task)null, errorList);
                            return returnValue;
                        }
                        else {
                            if(projectRepository.exists(projectId)) {
                                Task taskChild = new Task(taskChildId,taskChildName,projectId,taskParentId);
                                taskRepository.save(taskChild);
                                taskParent.setTaskChild(jpaTaskRepository.findByTaskParentId(taskParentId));
                                taskRepository.save(taskParent);
                                errorList.put(2, "added successful");
                                returnValue = new TaskReturnValue((Task)taskChild, errorList);
                                return returnValue;
                            }
                            else{
                                errorList.put(-403, "projectId not exist");
                                returnValue = new TaskReturnValue((Task)null, errorList);
                                return returnValue;
                            }
                        }
                    }
                }
                else{
                    errorList.put(-605, "employeeId not login");
                    returnValue = new TaskReturnValue((Task)null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/update",method=RequestMethod.POST)
    public TaskReturnValue updateTask(@RequestParam("taskId")String taskId,
                             @RequestParam("taskName")String taskName,
                             @RequestParam("employeeId")String employeeId,
                             HttpSession session) {
        HashMap errorList = new HashMap();
        TaskReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            errorList.put(-601, "employeeId not exist");
            returnValue = new TaskReturnValue((Task)null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null) {
                errorList.put(-100, "must login first");
                returnValue = new TaskReturnValue((Task)null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("employeeSession").equals(employee.getId())) {
                    Task task = taskRepository.findOne(taskId);
                    if(task==null){
                        errorList.put(-502, "taskId existed");
                        returnValue = new TaskReturnValue((Task)null, errorList);
                        return returnValue;
                    }
                    else {
                        task.setName(taskName);
                        taskRepository.save(task);
                        errorList.put(2, "updated successful");
                        returnValue = new TaskReturnValue((Task)task, errorList);
                        return returnValue;
                    }
                }
                else {
                    errorList.put(-605, "employeeId not login");
                    returnValue = new TaskReturnValue((Task)null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/listByProjectId",method=RequestMethod.GET)
    public List<Task> listTask(@RequestParam("projectId")String projectId){
        List<Task> listTask = jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectId);
        for(int i=0;i<listTask.size();i++){
            listTask.get(i).setTaskChild(jpaTaskRepository.findByTaskParentId(listTask.get(i).getId()));
            taskRepository.save(listTask.get(i));
        }
        taskRepository.save(listTask);
        return listTask;
    }

    @RequestMapping(value="/listTaskChildByTaskId",method=RequestMethod.GET)
    public List<Task> listTaskChild(@RequestParam("taskId")String taskId){
        List<Task> taskChildList = jpaTaskRepository.findByTaskParentId(taskId);
        taskRepository.save(taskChildList);
        return taskChildList;
    }

    @RequestMapping(value="/delTaskChild", method=RequestMethod.DELETE)
    public TaskReturnValue delTaskChild(@RequestParam("taskChildId")String taskChildId,
                             @RequestParam("employeeId")String employeeId,
                             HttpSession session) {
        HashMap errorList = new HashMap();
        TaskReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            errorList.put(-601, "employeeId not exist");
            returnValue = new TaskReturnValue((Task)null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null){
                errorList.put(-100, "must login first");
                returnValue = new TaskReturnValue((Task)null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("employeeSession").equals(employeeId)) {
                    Task taskChild = taskRepository.findOne(taskChildId);
                    if(taskChild==null){
                        errorList.put(-505, "taskChildId not exist");
                        returnValue = new TaskReturnValue((Task)null, errorList);
                        return returnValue;
                    }
                    else {
                        Task taskParent = taskRepository.findOne(taskChild.getTaskParentId());
                        if(taskParent==null){
                            errorList.put(-506, "taskParentId not exist");
                            returnValue = new TaskReturnValue((Task)null, errorList);
                            return returnValue;
                        }
                        else {
                            taskRepository.delete(taskChild);
                            taskParent.setTaskChild(jpaTaskRepository.findByTaskParentId(taskParent.getId()));
                            taskRepository.save(taskParent);
                            errorList.put(4, "deleted successful");
                            returnValue = new TaskReturnValue((Task)taskChild, errorList);
                            return returnValue;
                        }
                    }
                }
                else{
                    errorList.put(-605, "employeeId not login");
                    returnValue = new TaskReturnValue((Task)null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/delTask", method = RequestMethod.DELETE)
    public TaskReturnValue delTask(@RequestParam("taskId")String taskId,
                        @RequestParam("employeeId")String employeeId,
                        HttpSession session) {
        HashMap errorList = new HashMap();
        TaskReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null) {
            errorList.put(-601, "employeeId not exist");
            returnValue = new TaskReturnValue((Task) null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null) {
                errorList.put(-100, "must login first");
                returnValue = new TaskReturnValue((Task) null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("employeeSession").equals(employeeId)) {
                    Task task = taskRepository.findOne(taskId);
                    if (task == null) {
                        errorList.put(-507, "taskId not exist");
                        returnValue = new TaskReturnValue((Task) null, errorList);
                        return returnValue;
                    }
                    else {
                        Project project = projectRepository.findOne(task.getProjectId());
                        List<Task> taskChildList = jpaTaskRepository.findByTaskParentId(taskId);
                        if (taskChildList == null) {
                            errorList.put(-500, "taskChild is empty");
                            returnValue = new TaskReturnValue((Task) null, errorList);
                            return returnValue;
                        }
                        else {
                            for (int i = 0; i < taskChildList.size(); i++) {
                                taskRepository.delete(taskChildList.get(i));
                                //delTaskChild(taskChildList.get(i).getId());
                            }
                            taskRepository.delete(task);
                            project.setListTask(jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(project.getId()));
                            projectRepository.save(project);
                            errorList.put(4, "deleted successful");
                            returnValue = new TaskReturnValue((Task) task, errorList);
                            return returnValue;
                        }
                    }
                }
                else {
                    errorList.put(-605, "employeeId not login");
                    returnValue = new TaskReturnValue((Task) null, errorList);
                    return returnValue;
                }
            }
        }
    }
}
