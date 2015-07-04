package demo.bean;

import demo.model.Project;

import java.util.*;

/**
 * Created by Khang on 02/07/2015.
 */
public class ProjectReturnValue extends BasicReturnValue {
    private Project project;

    private List<Project> projectList;

    public ProjectReturnValue(){};

    public ProjectReturnValue(int resultCode){
        super(resultCode);
        this.setProject(project);
        this.setProjectList(projectList);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }
}
