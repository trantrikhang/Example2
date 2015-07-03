package demo.controller;

import demo.model.Company;
import demo.model.CompanyReturnValue;
import demo.model.Owner;
import demo.model.OwnerReturnValue;
import demo.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Created by Khang on 30/06/2015.
 */
@RestController
@RequestMapping("/owner")
@SessionAttributes({"ownerSession","employeeSession"})
public class OwnerController {
    @Autowired
    OwnerRepository ownerRepository;

    @RequestMapping(value="/add",method = RequestMethod.POST)
    public OwnerReturnValue addOwner(@RequestParam("ownerId")String ownerId,
                          @RequestParam("ownerName")String ownerName,
                          @RequestParam("ownerPassword")String ownerPassword){
        String salt = BCrypt.gensalt();
        HashMap errorList = new HashMap();
        OwnerReturnValue returnValue;
        if(ownerRepository.exists(ownerId)){
            errorList.put(-204, "ownerId existed");
            returnValue = new OwnerReturnValue(null, errorList);
            return returnValue;
        }
        else {
            Owner owner = new Owner(ownerId,ownerName,BCrypt.hashpw(ownerPassword, salt),salt);
            ownerRepository.save(owner);
            errorList.put(2, "added successful");
            returnValue = new OwnerReturnValue(owner, errorList);
            return returnValue;
        }
    }

    @RequestMapping(value="/update",method=RequestMethod.POST)
    public OwnerReturnValue updateOwner(@RequestParam("ownerId")String ownerId,
                             @RequestParam("ownerName")String ownerName,
                             @RequestParam("ownerPassword")String ownerPassword,
                              HttpSession session) {
        HashMap errorList = new HashMap();
        OwnerReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if(owner==null){
            errorList.put(-200, "ownerId not existed");
            returnValue = new OwnerReturnValue(null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null){
                errorList.put(-100, "must login first");
                returnValue = new OwnerReturnValue(null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("ownerSession").equals(owner.getId())) {
                    String salt = BCrypt.gensalt();
                    owner.setName(ownerName);
                    owner.setSalt(salt);
                    owner.setPassword(BCrypt.hashpw(ownerPassword, salt));
                    ownerRepository.save(owner);
                    errorList.put(3, "updated successful");
                    returnValue = new OwnerReturnValue(owner, errorList);
                    return returnValue;
                }
                else{
                    errorList.put(-202, "ownerId not login");
                    returnValue = new OwnerReturnValue(null, errorList);
                    return returnValue;
                }
            }
        }
    }
}
