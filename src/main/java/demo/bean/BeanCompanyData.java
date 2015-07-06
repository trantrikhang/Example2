package demo.bean;

import demo.model.*;
import java.io.Serializable;
import java.lang.Object;
import java.util.List;

/**
 * Created by Khang on 06/07/2015.
 */
public class BeanCompanyData implements Serializable {
    private String beanType;

    private Object beanData;

    public BeanCompanyData(){};

    public BeanCompanyData(String beanType){
        this.beanType = beanType;
        this.beanData = null;
    }

    public BeanCompanyData(String beanType, Company company){
        this.beanType = beanType;
        this.beanData = (Company) company;
    }

    public BeanCompanyData(String beanType, List<Company> companyList){
        this.beanType = beanType;
        this.beanData = (List<Company>) companyList;
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
