package demo.controller;

import demo.bean.LoginReturnValue;
import demo.model.*;
import demo.repository.EmployeeRepository;
import demo.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by Khang on 02/07/2015.
 */

@RestController
@RequestMapping("/login")
@SessionAttributes({"ownerSession","employeeSession"})
public class LoginController {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    OwnerRepository ownerRepository;

    @RequestMapping(value="/ownerLogin", method= RequestMethod.POST)
    public LoginReturnValue ownerLogin(@RequestParam("ownerId")String ownerIdInput,
                             @RequestParam("ownerPassword")String ownerPassword,
                             HttpSession session){
        LoginReturnValue returnValue;
        if(ownerIdInput.isEmpty()){
            returnValue = new LoginReturnValue(GeneralConstant.RESULT_CODE_OWNERID_EMPTY);
            return returnValue;
        }
        else {
            int ownerId = Integer.parseInt(ownerIdInput);
            Owner owner = ownerRepository.findOne(ownerId);
            if (owner == null) {
                returnValue = new LoginReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
                return returnValue;
            } else {
                if (ownerPassword.isEmpty()) {
                    returnValue = new LoginReturnValue(GeneralConstant.RESULT_CODE_PASSWORD_EMPTY);
                    return returnValue;
                } else {
                    if (BCrypt.checkpw(ownerPassword, owner.getPassword())) {
                        session.setAttribute("ownerSession", owner);
                        returnValue = new LoginReturnValue(GeneralConstant.RESULT_CODE_VALID);
                        return returnValue;
                    } else {
                        returnValue = new LoginReturnValue(GeneralConstant.RESULT_CODE_WRONG_PASSWORD);
                        return returnValue;
                    }
                }
            }
        }
    }

    @RequestMapping(value="/employeeLogin", method=RequestMethod.POST)
    public LoginReturnValue employeeLogin(@RequestParam("employeeId")String employeeIdInput,
                                @RequestParam("employeePassword")String employeePassword,
                                HttpSession session){
        LoginReturnValue returnValue;
        if(employeeIdInput.isEmpty()){
            returnValue = new LoginReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else {
            int employeeId = Integer.parseInt(employeeIdInput);
            Employee employee = employeeRepository.findOne(employeeId);
            if (employee == null) {
                returnValue = new LoginReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
                return returnValue;
            } else {
                if (employeePassword.isEmpty()) {
                    returnValue = new LoginReturnValue(GeneralConstant.RESULT_CODE_PASSWORD_EMPTY);
                    return returnValue;
                } else {
                    if (BCrypt.checkpw(employeePassword, employee.getPassword())) {//kiểm tra plaintext pass vừa nhập với encrypted pass
                        session.setAttribute("employeeSession", employee);//gán employeeId vào session
                        returnValue = new LoginReturnValue(GeneralConstant.RESULT_CODE_VALID);
                        return returnValue;
                    } else {
                        returnValue = new LoginReturnValue(GeneralConstant.RESULT_CODE_WRONG_PASSWORD);
                        return returnValue;
                    }
                }
            }
        }
    }

    @RequestMapping(value="/logout",method=RequestMethod.GET)
    public  LoginReturnValue logout(HttpSession session){
        LoginReturnValue returnValue;
        session.invalidate();
        returnValue = new LoginReturnValue(GeneralConstant.RESULT_CODE_VALID);
        return returnValue;
    }
}
