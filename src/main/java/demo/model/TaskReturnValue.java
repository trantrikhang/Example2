package demo.model;

import java.util.HashMap;

/**
 * Created by Khang on 02/07/2015.
 */
public class TaskReturnValue extends BasicReturnValue {
    private Task task;

    public TaskReturnValue(){};

    public TaskReturnValue(Task task,HashMap errorList){
        super(errorList);
        this.task=task;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
