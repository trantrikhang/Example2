package demo.controller;

import demo.model.*;
import demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Khang on 18/06/2015.
 */
@RestController
@RequestMapping("/employee")
@SessionAttributes({"ownerSession","employeeSession"})
public class EmployeeController {

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    JpaEmployeeRepository jpaEmployeeRepository;

    @RequestMapping(value ="/view",method= RequestMethod.GET)
    public EmployeeReturnValue viewEmployee(@RequestParam("employeeId")String employeeId) {
        HashMap errorList = new HashMap();
        EmployeeReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            errorList.put(-601, "employeeId not exist");
            returnValue = new EmployeeReturnValue((Employee) null, errorList);
            return returnValue;
        }
        else {
            errorList.put(1, "list successful");
            returnValue = new EmployeeReturnValue(employee, errorList);
            return returnValue;
        }
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    public EmployeeReturnValue addEmployee(@RequestParam("employeeId")String employeeId,
                              @RequestParam("employeeName")String employeeName,
                              @RequestParam("employeePassword")String employeePassword,
                              @RequestParam("companyId")String companyId,
                              @RequestParam("ownerId")String ownerId,
                              HttpSession session) {
        HashMap errorList = new HashMap();
        EmployeeReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if(owner==null){
            errorList.put(-200, "ownerId not exist");
            returnValue = new EmployeeReturnValue((Employee) null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null){
                errorList.put(-100, "must login first");
                returnValue = new EmployeeReturnValue((Employee) null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("ownerSession").equals(owner.getId())) {
                    String salt = BCrypt.gensalt();
                    if(employeeRepository.exists(employeeId)){
                        errorList.put(-602, "employeeId existed");
                        returnValue = new EmployeeReturnValue((Employee) null, errorList);
                        return returnValue;
                    }
                    else {
                        if (companyRepository.exists(companyId)) {//companyId phải tồn tại thì mới set được
                            Employee employee = new Employee(employeeId,employeeName,BCrypt.hashpw(employeePassword, salt),salt,companyId);
                            employeeRepository.save(employee);
                            errorList.put(2, "added successful");
                            returnValue = new EmployeeReturnValue(employee, errorList);
                            return returnValue;
                        }
                        else{
                            errorList.put(-300, "companyId not existed");
                            returnValue = new EmployeeReturnValue((Employee) null, errorList);
                            return returnValue;
                        }
                    }
                }
                else{
                    errorList.put(-202, "ownerId not login");
                    returnValue = new EmployeeReturnValue((Employee) null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/update",method=RequestMethod.PUT)
    public EmployeeReturnValue updateEmployee(@RequestParam("employeeId")String employeeId,
                                 @RequestParam("employeePassword")String employeePassword,
                                 @RequestParam("employeeName")String employeeName,
                                 @RequestParam("companyId")String companyId,
                                 HttpSession session) {
        HashMap errorList = new HashMap();
        EmployeeReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if(employee==null){
            errorList.put(-200, "ownerId not exist");
            returnValue = new EmployeeReturnValue((Employee) null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("employeeSession") == null){
                errorList.put(-100, "must login first");
                returnValue = new EmployeeReturnValue((Employee) null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("employeeSession").equals(employee.getId())) {
                    String salt = BCrypt.gensalt();
                    employee.setName(employeeName);
                    employee.setSalt(salt);
                    employee.setPassword(BCrypt.hashpw(employeePassword, salt));
                    if (companyRepository.exists(companyId)) {
                        employee.setCompanyId(companyId);
                        employeeRepository.save(employee);
                        errorList.put(3, "updated successful");
                        returnValue = new EmployeeReturnValue(employee, errorList);
                        return returnValue;
                    }
                    else {
                        errorList.put(-300, "companyId not existed");
                        returnValue = new EmployeeReturnValue((Employee) null, errorList);
                        return returnValue;
                    }
                }
                else{
                    errorList.put(-202, "ownerId not login");
                    returnValue = new EmployeeReturnValue((Employee) null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/del",method=RequestMethod.DELETE)
    public EmployeeReturnValue delEmployee(@RequestParam("employeeId") String employeeId,
                              @RequestParam("ownerId")String ownerId,
                              HttpSession session) {
        HashMap errorList = new HashMap();
        EmployeeReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if(owner==null){
            errorList.put(-200, "ownerId not exist");
            returnValue = new EmployeeReturnValue((Employee) null, errorList);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null){
                errorList.put(-100, "must login first");
                returnValue = new EmployeeReturnValue((Employee) null, errorList);
                return returnValue;
            }
            else {
                if (session.getAttribute("ownerSession").equals(owner.getId())) {
                    Employee employee = employeeRepository.findOne(employeeId);
                    if(employee==null){
                        errorList.put(-601, "employeeId not exist");
                        returnValue = new EmployeeReturnValue((Employee) null, errorList);
                        return returnValue;
                    }
                    else {
                        employeeRepository.delete(employee);
                        errorList.put(4, "deleted successful");
                        returnValue = new EmployeeReturnValue(employee, errorList);
                        return returnValue;
                    }
                }
                else {
                    errorList.put(-202, "ownerId not login");
                    returnValue = new EmployeeReturnValue((Employee) null, errorList);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/listAll", method=RequestMethod.GET)
    public Iterable<Employee> listAllEmployee(){
        Iterable<Employee> employeeList = employeeRepository.findAll();
        return employeeList;
    }
    @RequestMapping(value="/listByCompanyId",method=RequestMethod.GET)
    public List<Employee> listEmployeeByCompanyId(@RequestParam("companyId") String companyId){
        List<Employee> employeeList = jpaEmployeeRepository.findByCompanyId(companyId);
        return employeeList;
    }

    @RequestMapping(value="/listByProjectId",method=RequestMethod.GET)
    public List<Employee> listEmployeeByProjectId(@RequestParam("projectId")String projectId){
        List<Employee> employeeList = jpaEmployeeRepository.findByProjectId(projectId);
        return employeeList;
    }
}
