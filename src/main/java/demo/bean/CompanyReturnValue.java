package demo.bean;

import demo.model.Company;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Khang on 02/07/2015.
 */
public class CompanyReturnValue extends BasicReturnValue{

    private Company company;

    private List<Company> companyList;

    public CompanyReturnValue(){};

    public CompanyReturnValue(int resultCode){
        super(resultCode);
        this.setCompany(company);
        this.setCompanyList(companyList);
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<Company> getCompanyList() {
        return companyList;
    }

    public void setCompanyList(List<Company> companyList) {
        this.companyList = companyList;
    }
}
