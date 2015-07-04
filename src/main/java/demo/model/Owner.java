package demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Khang on 30/06/2015.
 */
@Entity
@Table(name="owner")
public class Owner extends People {

    @Column(name="company_id")
    private Integer companyId;

    protected Owner(){}

    public Owner(String name, String password, Integer companyId){
        super(name,password);
        this.companyId=companyId;
    }

    public Owner(String name, String password){
        super(name,password);
        this.companyId=null;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
