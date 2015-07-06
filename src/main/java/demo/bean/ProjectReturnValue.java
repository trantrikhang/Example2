package demo.bean;

import demo.model.Project;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Khang on 02/07/2015.
 */
public class ProjectReturnValue extends BeanRootReturnValue implements Serializable{
    private BeanProjectData bean;

    public ProjectReturnValue(){};

    public ProjectReturnValue(int resultCode){
        super(resultCode);
        bean = new BeanProjectData("Project Item");
    }

    public ProjectReturnValue(Project project,int resultCode){
        super(resultCode);
        bean = new BeanProjectData("Project Item", project);
    }

    public ProjectReturnValue(List<Project> projectList,int resultCode){
        super(resultCode);
        bean = new BeanProjectData("Project List", projectList);
    }

    public BeanProjectData getBean() {
        return bean;
    }

    public void setBean(BeanProjectData bean) {
        this.bean = bean;
    }
}
