package demo.bean;

import demo.model.Task;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Khang on 02/07/2015.
 */
public class TaskReturnValue extends BasicReturnValue {
    private Task task;

    private List<Task> taskList;
    public TaskReturnValue(){};

    public TaskReturnValue(int resultCode){
        super(resultCode);
        this.setTask(task);
        this.setTaskList(taskList);
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }
}
