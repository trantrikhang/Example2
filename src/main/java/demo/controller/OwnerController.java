package demo.controller;

import demo.model.GeneralConstant;
import demo.model.Owner;
import demo.bean.OwnerReturnValue;
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
    public OwnerReturnValue addOwner(@RequestParam("ownerName")String ownerName,
                          @RequestParam("ownerPassword")String ownerPassword) {
        String salt = BCrypt.gensalt();
        OwnerReturnValue returnValue;
        Owner owner = new Owner(ownerName, BCrypt.hashpw(ownerPassword, salt));
        ownerRepository.save(owner);
        returnValue = new OwnerReturnValue(owner, GeneralConstant.RESULT_CODE_VALID);
        return returnValue;
    }

    @RequestMapping(value="/update",method=RequestMethod.POST)
    public OwnerReturnValue updateOwner(@RequestParam("ownerId")Integer ownerId,
                             @RequestParam("ownerName")String ownerName,
                             @RequestParam("ownerPassword")String ownerPassword,
                              HttpSession session) {
        OwnerReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if(owner==null){
            returnValue = new OwnerReturnValue(null, GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null){
                returnValue = new OwnerReturnValue(null, GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            }
            else {
                Owner ownerSession = (Owner) session.getAttribute("ownerSession");
                if (ownerSession.getId().equals(owner.getId())) {
                    String salt = BCrypt.gensalt();
                    owner.setName(ownerName);
                    owner.setPassword(BCrypt.hashpw(ownerPassword, salt));
                    ownerRepository.save(owner);
                    returnValue = new OwnerReturnValue(owner, GeneralConstant.RESULT_CODE_VALID);
                    return returnValue;
                }
                else{
                    returnValue = new OwnerReturnValue(null, GeneralConstant.RESULT_CODE_OWNERID_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }
}
