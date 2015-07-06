package demo.bean;

import demo.model.Task;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Khang on 02/07/2015.
 */
public class TaskReturnValue extends BeanRootReturnValue implements Serializable {
    private BeanTaskData bean;

    public TaskReturnValue(){};

    public TaskReturnValue(int resultCode){
        super(resultCode);
        bean = new BeanTaskData("Task Item");
    }

    public TaskReturnValue(Task task,int resultCode){
        super(resultCode);
        bean = new BeanTaskData("Task Item", task);
    }

    public TaskReturnValue(List<Task> taskList,int resultCode){
        super(resultCode);
        bean = new BeanTaskData("Task List", taskList);
    }

    public BeanTaskData getBean() {
        return bean;
    }

    public void setBean(BeanTaskData bean) {
        this.bean = bean;
    }
}
