package demo.bean;

import demo.model.Employee;
import java.io.Serializable;
import java.util.*;

/**
 * Created by Khang on 02/07/2015.
 */
public class EmployeeReturnValue extends BeanRootReturnValue implements Serializable{
    private BeanEmployeeData bean;

    public EmployeeReturnValue(){};

    public EmployeeReturnValue(int resultCode){
        super(resultCode);
        bean = new BeanEmployeeData("Employee Item");
    }

    public EmployeeReturnValue(Employee employee,int resultCode){
        super(resultCode);
        bean = new BeanEmployeeData("Employee Item", employee);
    }

    public EmployeeReturnValue(List<Employee> employeeList,int resultCode){
        super(resultCode);
        bean = new BeanEmployeeData("Employee List", employeeList);
    }

    public BeanEmployeeData getBean() {
        return bean;
    }

    public void setBean(BeanEmployeeData bean) {
        this.bean = bean;
    }
}
