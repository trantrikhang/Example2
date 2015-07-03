package demo.model;

import java.util.HashMap;

/**
 * Created by Khang on 03/07/2015.
 */
public class LoginReturnValue extends BasicReturnValue{
    public  LoginReturnValue(){};

    public LoginReturnValue(HashMap errorList){
        super(errorList);
    }
}
