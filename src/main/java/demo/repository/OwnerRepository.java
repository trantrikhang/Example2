package demo.repository;


import demo.model.Owner;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Khang on 30/06/2015.
 */
public interface OwnerRepository extends CrudRepository<Owner,String> {
}
