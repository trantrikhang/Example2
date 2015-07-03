package demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Khang on 30/06/2015.
 */
@Entity
@Table(name="owner")
public class Owner extends People {

    private String companyId;

    protected Owner(){}

    public Owner(String id, String name, String password, String salt, String companyId){
        super(id,name,password,salt);
        this.companyId=companyId;
    }

    public Owner(String id, String name, String password, String salt){
        super(id,name,password,salt);
        this.companyId=null;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
