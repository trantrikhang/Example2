package demo.bean;

import demo.model.Owner;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Khang on 02/07/2015.
 */
public class OwnerReturnValue extends BeanRootReturnValue implements Serializable{
    private BeanOwnerData bean;

    public OwnerReturnValue(){};

    public OwnerReturnValue(int resultCode){
        super(resultCode);
        bean = new BeanOwnerData("Owner Item");
    }

    public OwnerReturnValue(Owner owner,int resultCode){
        super(resultCode);
        bean = new BeanOwnerData("Owner Item", owner);
    }

    public OwnerReturnValue(List<Owner> ownerList,int resultCode){
        super(resultCode);
        bean = new BeanOwnerData("Owner List", ownerList);
    }

    public BeanOwnerData getBean() {
        return bean;
    }

    public void setBean(BeanOwnerData bean) {
        this.bean = bean;
    }
}
