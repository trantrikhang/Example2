package demo.controller;

import demo.bean.TaskReturnValue;
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
    public TaskReturnValue addTaskToProject(@RequestParam("taskName")String taskName,
                                   @RequestParam("projectId")Integer projectId,
                                   @RequestParam("employeeId")Integer employeeId,
                                   HttpSession session) {
        TaskReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null) {
                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            }
            else {
                Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                if (employeeSession.getId().equals(employee.getId())) {
                    Project project = projectRepository.findOne(projectId);
                    if (project == null) {
                        returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                        return returnValue;
                    } else {
                        if (projectRepository.exists(projectId)) {
                            Task task = new Task(taskName, projectId);
                            taskRepository.save(task);
                            project.setListTask(jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectId));
                            projectRepository.save(project);
                            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_VALID);
                            returnValue.setTask(task);
                            returnValue.setTaskList(null);
                            return returnValue;
                        } else {
                            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                            return returnValue;
                        }
                    }
                }
                else {
                    returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/addTaskChildToTask",method=RequestMethod.POST)
    public TaskReturnValue addTaskChildToTask(@RequestParam("taskChildName")String taskChildName,
                                     @RequestParam("projectId")Integer projectId,
                                     @RequestParam("taskParentId")Integer taskParentId,
                                     @RequestParam("employeeId")Integer employeeId,
                                     HttpSession session) {
        TaskReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null){
                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            }
            else {
                Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                if (employeeSession.getId().equals(employee.getId())) {
                    Task taskParent = taskRepository.findOne(taskParentId);
                    if (taskParent == null) {
                        returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKPARENTID_NOT_EXIST);
                        return returnValue;
                    } else {
                        if (projectRepository.exists(projectId)) {
                            Task taskChild = new Task(taskChildName, projectId, taskParentId);
                            taskRepository.save(taskChild);
                            taskParent.setTaskChild(jpaTaskRepository.findByTaskParentId(taskParentId));
                            taskRepository.save(taskParent);
                            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_VALID);
                            returnValue.setTask(taskChild);
                            returnValue.setTaskList(null);
                            return returnValue;
                        } else {
                            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                            return returnValue;
                        }
                    }
                }
                else{
                    returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/update",method=RequestMethod.POST)
    public TaskReturnValue updateTask(@RequestParam("taskId")Integer taskId,
                             @RequestParam("taskName")String taskName,
                             @RequestParam("employeeId")Integer employeeId,
                             HttpSession session) {
        TaskReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null) {
                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            }
            else {
                Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                if (employeeSession.getId().equals(employee.getId())) {
                    Task task = taskRepository.findOne(taskId);
                    if(task==null){
                        returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKID_EXISTED);
                        return returnValue;
                    }
                    else {
                        task.setName(taskName);
                        taskRepository.save(task);
                        returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_VALID);
                        returnValue.setTask(task);
                        returnValue.setTaskList(null);
                        return returnValue;
                    }
                }
                else {
                    returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/delTaskChild", method=RequestMethod.POST)
    public TaskReturnValue delTaskChild(@RequestParam("taskChildId")Integer taskChildId,
                             @RequestParam("employeeId")Integer employeeId,
                             HttpSession session) {
        TaskReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null){
                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            }
            else {
                Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                if (employeeSession.getId().equals(employeeId)) {
                    Task taskChild = taskRepository.findOne(taskChildId);
                    if(taskChild==null){
                        returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKCHILDID_NOT_EXIST);
                        return returnValue;
                    }
                    else {
                        Task taskParent = taskRepository.findOne(taskChild.getTaskParentId());
                        if(taskParent==null){
                            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKPARENTID_NOT_EXIST);
                            return returnValue;
                        }
                        else {
                            taskRepository.delete(taskChild);
                            taskParent.setTaskChild(jpaTaskRepository.findByTaskParentId(taskParent.getId()));
                            taskRepository.save(taskParent);
                            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_VALID);
                            returnValue.setTask(null);
                            returnValue.setTaskList(null);
                            return returnValue;
                        }
                    }
                }
                else{
                    returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/delTask", method = RequestMethod.POST)
    public TaskReturnValue delTask(@RequestParam("taskId")Integer taskId,
                        @RequestParam("employeeId")Integer employeeId,
                        HttpSession session) {
        TaskReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null) {
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null) {
                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            }
            else {
                Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                if (employeeSession.getId().equals(employeeId)) {
                    Task task = taskRepository.findOne(taskId);
                    if (task == null) {
                        returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKID_NOT_EXIST);
                        return returnValue;
                    }
                    else {
                        Project project = projectRepository.findOne(task.getProjectId());
                        List<Task> taskChildList = jpaTaskRepository.findByTaskParentId(taskId);
                        if (taskChildList == null) {
                            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKCHILDLIST_EMPTY);
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
                            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_VALID);
                            returnValue.setTask(null);
                            returnValue.setTaskList(null);
                            return returnValue;
                        }
                    }
                }
                else {
                    returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/listByProjectId",method=RequestMethod.GET)
    public TaskReturnValue listTask(@RequestParam("projectId")Integer projectId){
        TaskReturnValue returnValue;
        List<Task> taskList = jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectId);
        if(taskList==null) {
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKCHILDLIST_EMPTY);
            return returnValue;
        }
        else {
            for (int i = 0; i < taskList.size(); i++) {
                taskList.get(i).setTaskChild(jpaTaskRepository.findByTaskParentId(taskList.get(i).getId()));
                taskRepository.save(taskList.get(i));
            }
            taskRepository.save(taskList);
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_VALID);
            returnValue.setTaskList(taskList);
            returnValue.setTask(null);
            return returnValue;
        }
    }

    @RequestMapping(value="/listTaskChildByTaskId",method=RequestMethod.GET)
    public TaskReturnValue listTaskChild(@RequestParam("taskId")Integer taskId){
        TaskReturnValue returnValue;
        List<Task> taskChildList = jpaTaskRepository.findByTaskParentId(taskId);
        if(taskChildList==null) {
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKCHILDLIST_EMPTY);
            return returnValue;
        }
        else {
            taskRepository.save(taskChildList);
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_VALID);
            returnValue.setTaskList(taskChildList);
            returnValue.setTask(null);
            return returnValue;
        }
    }
}
