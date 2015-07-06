package demo.bean;

import demo.model.Task;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Khang on 06/07/2015.
 */
public class BeanTaskData implements Serializable {
    private String beanType;

    private Object beanData;

    public BeanTaskData(){};

    public BeanTaskData(String beanType){
        this.beanType = beanType;
        this.beanData = null;
    }

    public BeanTaskData(String beanType, Task task){
        this.beanType = beanType;
        this.beanData = (Task) task;
    }

    public BeanTaskData(String beanType, List<Task> taskList){
        this.beanType = beanType;
        this.beanData = (List<Task>) taskList;
    }

    public String getBeanType() {
        return beanType;
    }

    public void setBeanType(String beanType) {
        this.beanType = beanType;
    }

    public Object getBeanData() {
        return beanData;
    }

    public void setBeanData(Object beanData) {
        this.beanData = beanData;
    }
}
