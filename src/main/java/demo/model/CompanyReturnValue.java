package demo.model;

import java.util.HashMap;

/**
 * Created by Khang on 02/07/2015.
 */
public class CompanyReturnValue extends BasicReturnValue{

    private Company company;

    private Iterable<Company> listCompany;

    public CompanyReturnValue(){};

    public CompanyReturnValue(Company company,HashMap errorList){
        super(errorList);
        this.company = company;
    }

    public CompanyReturnValue(Iterable<Company> listCompany,HashMap errorList){
        super(errorList);
        this.listCompany = listCompany;
    }

//    public CompanyReturnValue(Company company){
//        this.company = company;
//    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
