package demo.controller;

import demo.model.Owner;
import demo.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Id;

/**
 * Created by Khang on 30/06/2015.
 */
@RestController
@RequestMapping("/owner")
public class OwnerController {
    @Autowired
    OwnerRepository ownerRepository;

    @RequestMapping(value="/add",method = RequestMethod.POST)
    public Owner addOwner(@RequestParam("ownerId")String ownerId,
                          @RequestParam("ownerName")String ownerName,
                          @RequestParam("ownerPassword")String ownerPassword){
        Owner owner = new Owner();
        owner.setOwnerId(ownerId);
        owner.setOwnerName(ownerName);
        owner.setOwnerPassword(ownerPassword);
        ownerRepository.save(owner);
        return owner;
    }

    @RequestMapping(value="/update",method=RequestMethod.POST)
    public Owner updateOwner(@RequestParam("ownerId")String ownerId,
                          @RequestParam("ownerName")String ownerName,
                          @RequestParam("ownerPassword")String ownerPassword){
        Owner owner = ownerRepository.findOne(ownerId);
        owner.setOwnerName(ownerName);
        owner.setOwnerPassword(ownerPassword);
        ownerRepository.save(owner);
        return owner;
    }
}
