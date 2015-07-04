package demo.repository;

import demo.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Khang on 01/07/2015.
 */
public interface JpaProjectRepository extends JpaRepository<Project,Integer> {
    List<Project> findByCompanyId (Integer companyId);
}
