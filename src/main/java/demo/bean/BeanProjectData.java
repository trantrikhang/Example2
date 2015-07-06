package demo.bean;

import demo.model.Project;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Khang on 06/07/2015.
 */
public class BeanProjectData implements Serializable{
    private String beanType;

    private Object beanData;

    public BeanProjectData(){};

    public BeanProjectData(String beanType){
        this.beanType = beanType;
        this.beanData = null;
    }

    public BeanProjectData(String beanType, Project project){
        this.beanType = beanType;
        this.beanData = (Project) project;
    }

    public BeanProjectData(String beanType, List<Project> projectList){
        this.beanType = beanType;
        this.beanData = (List<Project>) projectList;
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
