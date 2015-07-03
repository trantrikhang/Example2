package demo.model;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Khang on 02/07/2015.
 */
public abstract class BasicReturnValue {
    private HashMap<Integer,String> errorList;

    
    public BasicReturnValue(){};

    public BasicReturnValue(HashMap errorList){
        this.errorList = errorList;
    }

    public HashMap<Integer, String> getErrorList() {
        return errorList;
    }

    public void setErrorList(HashMap<Integer, String> errorList) {
        this.errorList = errorList;
    }
}
