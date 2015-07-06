package demo.bean;

import demo.model.Company;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Khang on 02/07/2015.
 */
public class CompanyReturnValue extends BeanRootReturnValue implements Serializable {

    private BeanCompanyData bean;

    public CompanyReturnValue(){};

    public CompanyReturnValue(int resultCode){
        super(resultCode);
        bean = new BeanCompanyData("Company Item");
    }

    public CompanyReturnValue(Company company,int resultCode){
        super(resultCode);
        bean = new BeanCompanyData("Company Item", company);
    }

    public CompanyReturnValue(List<Company> companyList,int resultCode){
        super(resultCode);
        bean = new BeanCompanyData("Company List", companyList);
    }

    public BeanCompanyData getBean() {
        return bean;
    }

    public void setBean(BeanCompanyData bean) {
        this.bean = bean;
    }
}
