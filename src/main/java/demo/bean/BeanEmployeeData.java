package demo.bean;

import demo.model.Employee;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Khang on 06/07/2015.
 */
public class BeanEmployeeData implements Serializable {
    private String beanType;

    private Object beanData;

    public BeanEmployeeData(){};

    public BeanEmployeeData(String beanType){
        this.beanType = beanType;
        this.beanData = null;
    }

    public BeanEmployeeData(String beanType, Employee employee){
        this.beanType = beanType;
        this.beanData = (Employee) employee;
    }

    public BeanEmployeeData(String beanType, List<Employee> employeeList){
        this.beanType = beanType;
        this.beanData = (List<Employee>) employeeList;
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
