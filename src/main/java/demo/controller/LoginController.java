package demo.controller;

import demo.model.*;
import demo.repository.CompanyRepository;
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

    @RequestMapping(value="/ownerLogin", method= RequestMethod.GET)
    public LoginReturnValue ownerLogin(@RequestParam("ownerId")String ownerId,
                             @RequestParam("ownerPassword")String ownerPassword,
                             HttpSession session){
        HashMap errorList = new HashMap();
        LoginReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if(owner==null){
            errorList.put(-200, "ownerId not exist");
            returnValue = new LoginReturnValue(errorList);
            return returnValue;
        }
        else {
            if (BCrypt.checkpw(ownerPassword, owner.getPassword())) {
                session.setAttribute("ownerSession", owner.getId());
                errorList.put(5, "login successful");
                returnValue = new LoginReturnValue(errorList);
                return returnValue;
            }
            else{
                errorList.put(-101, "wrong password");
                returnValue = new LoginReturnValue(errorList);
                return returnValue;
            }
        }
    }

    @RequestMapping(value="/employeeLogin", method=RequestMethod.GET)
    public LoginReturnValue employeeLogin(@RequestParam("employeeId")String employeeId,
                                @RequestParam("employeePassword")String employeePassword,
                                HttpSession session){
        HashMap errorList = new HashMap();
        LoginReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            errorList.put(-601, "employeeId not exist");
            returnValue = new LoginReturnValue(errorList);
            return returnValue;
        }
        else {
            if (BCrypt.checkpw(employeePassword, employee.getPassword())) {//kiểm tra plaintext pass vừa nhập với encrypted pass
                session.setAttribute("employeeSession", employee.getId());//gán employeeId vào session
                errorList.put(5, "login successful");
                returnValue = new LoginReturnValue(errorList);
                return returnValue;
            }
            else {
                errorList.put(-101, "wrong password");
                returnValue = new LoginReturnValue(errorList);
                return returnValue;
            }
        }
    }

    @RequestMapping(value="/logout",method=RequestMethod.GET)
    public  LoginReturnValue logout(HttpSession session){
        HashMap errorList = new HashMap();
        LoginReturnValue returnValue;
        session.invalidate();
        errorList.put(6, "logout successful");
        returnValue = new LoginReturnValue(errorList);
        return returnValue;
    }
}
