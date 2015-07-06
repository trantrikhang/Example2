package demo.bean;

import demo.model.Owner;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Khang on 06/07/2015.
 */
public class BeanOwnerData implements Serializable {
    private String beanType;

    private Object beanData;

    public BeanOwnerData(){};

    public BeanOwnerData(String beanType){
        this.beanType = beanType;
        this.beanData = null;
    }

    public BeanOwnerData(String beanType, Owner owner){
        this.beanType = beanType;
        this.beanData = (Owner) owner;
    }

    public BeanOwnerData(String beanType, List<Owner> ownerList){
        this.beanType = beanType;
        this.beanData = (List<Owner>) ownerList;
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
