package demo.repository;


import demo.model.Owner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Khang on 30/06/2015.
 */
public interface OwnerRepository extends CrudRepository<Owner,String> {
    @Query("select owner from Owner owner where owner.ownerId=?1 and owner.ownerPassword=?2")
    Owner getOwner(String ownerId, String ownerPassword);
}
