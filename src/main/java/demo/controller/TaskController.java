package demo.controller;

import demo.bean.ProjectReturnValue;
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
                                   @RequestParam("projectId")String projectIdInput,
                                   @RequestParam("projectManagerId")String projectManagerIdInput,
                                   HttpSession session) {
        TaskReturnValue returnValue;
        if (projectIdInput.isEmpty()) {
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_EMPTY);
            return returnValue;
        }
        else
        if(projectManagerIdInput.isEmpty()){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else {
            int projectId = Integer.parseInt(projectIdInput);
            int projectManagerId = Integer.parseInt(projectManagerIdInput);
            taskName = taskName.trim();
            Employee employee = employeeRepository.findOne(projectManagerId);
            if (employee == null) {
                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
                return returnValue;
            } else {
                if (session.getAttribute("employeeSession") == null) {
                    returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                } else {
                    Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                    if (employeeSession.getId().equals(employee.getId())) {
                        Project project = projectRepository.findOne(projectId);
                        if (project == null) {
                            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                            return returnValue;
                        } else {
                            if (projectRepository.exists(projectId)) {
                                if (taskName.isEmpty()) {
                                    returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKNAME_EMPTY);
                                    return returnValue;
                                } else {
                                    Task task = new Task(taskName, projectId);
                                    taskRepository.save(task);
                                    project.setListTask(jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectId));
                                    projectRepository.save(project);
                                    returnValue = new TaskReturnValue(task, GeneralConstant.RESULT_CODE_VALID);
                                    return returnValue;
                                }
                            } else {
                                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                                return returnValue;
                            }
                        }
                    } else {
                        returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                        return returnValue;
                    }
                }
            }
        }
    }

    @RequestMapping(value="/addTaskChildToTask",method=RequestMethod.POST)
    public TaskReturnValue addTaskChildToTask(@RequestParam("taskChildName")String taskChildName,
                                     @RequestParam("projectId")String projectIdInput,
                                     @RequestParam("taskParentId")String taskParentIdInput,
                                     @RequestParam("projectManagerId")String projectManagerIdInput,
                                     HttpSession session) {
        TaskReturnValue returnValue;
        if (projectIdInput.isEmpty()) {
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_EMPTY);
            return returnValue;
        }
        else
        if(projectManagerIdInput.isEmpty()){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else
        if(taskParentIdInput.isEmpty()){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKID_EMPTY);
            return returnValue;
        }
        else {
            int projectId = Integer.parseInt(projectIdInput);
            int projectManagerId = Integer.parseInt(projectManagerIdInput);
            int taskParentId = Integer.parseInt(taskParentIdInput);
            taskChildName = taskChildName.trim();
            Employee employee = employeeRepository.findOne(projectManagerId);
            if (employee == null) {
                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
                return returnValue;
            } else {
                if (session.getAttribute("employeeSession") == null) {
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
                                if (taskChildName.isEmpty()) {
                                    returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKNAME_EMPTY);
                                    return returnValue;
                                } else {
                                    Task taskChild = new Task(taskChildName, projectId, taskParentId);
                                    taskRepository.save(taskChild);
                                    taskParent.setTaskChild(jpaTaskRepository.findByTaskParentId(taskParentId));
                                    taskRepository.save(taskParent);
                                    returnValue = new TaskReturnValue(taskChild, GeneralConstant.RESULT_CODE_VALID);
                                    return returnValue;
                                }
                            } else {
                                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_NOT_EXIST);
                                return returnValue;
                            }
                        }
                    } else {
                        returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                        return returnValue;
                    }
                }
            }
        }
    }

    @RequestMapping(value="/update",method=RequestMethod.POST)
    public TaskReturnValue updateTask(@RequestParam("taskId")String taskIdInput,
                             @RequestParam("taskName")String taskName,
                             @RequestParam("projectManagerId")String projectManagerIdInput,
                             HttpSession session) {
        TaskReturnValue returnValue;
        if(projectManagerIdInput.isEmpty()){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else
        if(taskIdInput.isEmpty()){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKID_EMPTY);
            return returnValue;
        }
        else {
            int projectManagerId = Integer.parseInt(projectManagerIdInput);
            int taskId = Integer.parseInt(taskIdInput);
            taskName = taskName.trim();
            Employee employee = employeeRepository.findOne(projectManagerId);
            if (employee == null) {
                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
                return returnValue;
            } else {
                if (session.getAttribute("employeeSession") == null) {
                    returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                } else {
                    Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                    if (employeeSession.getId().equals(employee.getId())) {
                        Task task = taskRepository.findOne(taskId);
                        if (task == null) {
                            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKID_EXISTED);
                            return returnValue;
                        } else {
                            if (taskName.isEmpty()) {
                                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKNAME_EMPTY);
                                return returnValue;
                            } else {
                                task.setName(taskName);
                                taskRepository.save(task);
                                returnValue = new TaskReturnValue(task, GeneralConstant.RESULT_CODE_VALID);
                                return returnValue;
                            }
                        }
                    } else {
                        returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                        return returnValue;
                    }
                }
            }
        }
    }

    @RequestMapping(value="/delTaskChild", method=RequestMethod.POST)
    public TaskReturnValue delTaskChild(@RequestParam("taskChildId")String taskChildIdInput,
                             @RequestParam("projectManagerId")String projectManagerIdInput,
                             HttpSession session) {
        TaskReturnValue returnValue;
        if(projectManagerIdInput.isEmpty()){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else
        if(taskChildIdInput.isEmpty()){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKID_EMPTY);
            return returnValue;
        }
        else {
            int projectManagerId = Integer.parseInt(projectManagerIdInput);
            int taskChildId = Integer.parseInt(taskChildIdInput);
            Employee employee = employeeRepository.findOne(projectManagerId);
            if (employee == null) {
                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
                return returnValue;
            } else {
                if (session.getAttribute("employeeSession") == null) {
                    returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                } else {
                    Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                    if (employeeSession.getId().equals(projectManagerId)) {
                        Task taskChild = taskRepository.findOne(taskChildId);
                        if (taskChild == null) {
                            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKCHILDID_NOT_EXIST);
                            return returnValue;
                        } else {
                            Task taskParent = taskRepository.findOne(taskChild.getTaskParentId());
                            if (taskParent == null) {
                                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKPARENTID_NOT_EXIST);
                                return returnValue;
                            } else {
                                taskRepository.delete(taskChild);
                                taskParent.setTaskChild(jpaTaskRepository.findByTaskParentId(taskParent.getId()));
                                taskRepository.save(taskParent);
                                returnValue = new TaskReturnValue(taskParent, GeneralConstant.RESULT_CODE_VALID);
                                return returnValue;
                            }
                        }
                    } else {
                        returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                        return returnValue;
                    }
                }
            }
        }
    }

    @RequestMapping(value="/delTask", method = RequestMethod.POST)
    public TaskReturnValue delTask(@RequestParam("taskId")String taskIdInput,
                        @RequestParam("projectManagerId")String projectManagerIdInput,
                        HttpSession session) {
        TaskReturnValue returnValue;
        if(projectManagerIdInput.isEmpty()){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else
        if(taskIdInput.isEmpty()){
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKID_EMPTY);
            return returnValue;
        }
        else {
            int projectManagerId = Integer.parseInt(projectManagerIdInput);
            int taskId = Integer.parseInt(taskIdInput);
            Employee employee = employeeRepository.findOne(projectManagerId);
            if (employee == null) {
                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
                return returnValue;
            } else {
                if (session.getAttribute("employeeSession") == null) {
                    returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                    return returnValue;
                } else {
                    Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                    if (employeeSession.getId().equals(projectManagerId)) {
                        Task task = taskRepository.findOne(taskId);
                        if (task == null) {
                            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKID_NOT_EXIST);
                            return returnValue;
                        } else {
                            Project project = projectRepository.findOne(task.getProjectId());
                            List<Task> taskChildList = jpaTaskRepository.findByTaskParentId(taskId);
                            if (taskChildList == null) {
                                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKCHILDLIST_EMPTY);
                                return returnValue;
                            } else {
                                for (int i = 0; i < taskChildList.size(); i++) {
                                    taskRepository.delete(taskChildList.get(i));
                                    //delTaskChild(taskChildList.get(i).getId());
                                }
                                taskRepository.delete(task);
                                project.setListTask(jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(project.getId()));
                                projectRepository.save(project);
                                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_VALID);
                                return returnValue;
                            }
                        }
                    } else {
                        returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                        return returnValue;
                    }
                }
            }
        }
    }

    @RequestMapping(value="/listByProjectId",method=RequestMethod.GET)
    public TaskReturnValue listTask(@RequestParam("projectId")String projectIdInput) {
        TaskReturnValue returnValue;
        if (projectIdInput.isEmpty()) {
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_EMPTY);
            return returnValue;
        } else {
            int projectId = Integer.parseInt(projectIdInput);
            List<Task> taskList = jpaTaskRepository.findByProjectIdAndTaskParentIdIsNull(projectId);
            if (taskList == null) {
                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKCHILDLIST_EMPTY);
                return returnValue;
            } else {
                for (int i = 0; i < taskList.size(); i++) {
                    taskList.get(i).setTaskChild(jpaTaskRepository.findByTaskParentId(taskList.get(i).getId()));
                    taskRepository.save(taskList.get(i));
                }
                taskRepository.save(taskList);
                returnValue = new TaskReturnValue(taskList, GeneralConstant.RESULT_CODE_VALID);
                return returnValue;
            }
        }
    }

    @RequestMapping(value="/listTaskChildByTaskId",method=RequestMethod.GET)
    public TaskReturnValue listTaskChild(@RequestParam("taskId")String taskIdInput) {
        TaskReturnValue returnValue;
        if (taskIdInput.isEmpty()) {
            returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKID_EMPTY);
            return returnValue;
        } else {
            int taskId = Integer.parseInt(taskIdInput);
            List<Task> taskChildList = jpaTaskRepository.findByTaskParentId(taskId);
            if (taskChildList == null) {
                returnValue = new TaskReturnValue(GeneralConstant.RESULT_CODE_TASKCHILDLIST_EMPTY);
                return returnValue;
            } else {
                taskRepository.save(taskChildList);
                returnValue = new TaskReturnValue(taskChildList, GeneralConstant.RESULT_CODE_VALID);
                return returnValue;
            }
        }
    }
}
