package demo.controller;

import demo.bean.EmployeeReturnValue;
import demo.bean.TaskReturnValue;
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

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public EmployeeReturnValue viewEmployee(@RequestParam("employeeId") Integer employeeId) {

        EmployeeReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null) {
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
            return returnValue;
        } else {
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_VALID);
            return returnValue;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public EmployeeReturnValue addEmployee(@RequestParam("employeeName") String employeeName,
                                           @RequestParam("employeePassword") String employeePassword,
                                           @RequestParam("companyId") Integer companyId,
                                           @RequestParam("ownerId") Integer ownerId,
                                           HttpSession session) {
        EmployeeReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if (owner == null) {
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
            return returnValue;
        } else {
            if (session.getAttribute("ownerSession") == null) {
                returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            } else {
                Owner ownerSession = (Owner) session.getAttribute("ownerSession");
                if (ownerSession.getId().equals(owner.getId())) {
                    String salt = BCrypt.gensalt();

                    if (companyRepository.exists(companyId)) {//companyId phải tồn tại thì mới set được
                        Employee employee = new Employee(employeeName, BCrypt.hashpw(employeePassword, salt), companyId);
                        employeeRepository.save(employee);
                        returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_VALID);
                        returnValue.setEmployee(employee);
                        returnValue.setEmployeeList(null);
                        return returnValue;
                    } else {
                        returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_NOT_EXIST);
                        return returnValue;
                    }
                } else {
                    returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public EmployeeReturnValue updateEmployee(@RequestParam("employeeId") Integer employeeId,
                                              @RequestParam("employeePassword") String employeePassword,
                                              @RequestParam("employeeName") String employeeName,
                                              @RequestParam("companyId") Integer companyId,
                                              HttpSession session) {
        EmployeeReturnValue returnValue;
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null) {
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
            return returnValue;
        } else {
            if (session.getAttribute("employeeSession") == null) {
                returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            } else {
                Employee employeeSession = (Employee) session.getAttribute("employeeSession");
                if (employeeSession.getId().equals(employee.getId())) {
                    String salt = BCrypt.gensalt();
                    employee.setName(employeeName);
                    employee.setPassword(BCrypt.hashpw(employeePassword, salt));
                    if (companyRepository.exists(companyId)) {
                        employee.setCompanyId(companyId);
                        employeeRepository.save(employee);
                        returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_VALID);
                        returnValue.setEmployee(employee);
                        returnValue.setEmployeeList(null);
                        return returnValue;
                    } else {
                        returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_NOT_EXIST);
                        return returnValue;
                    }
                } else
                    returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_LOGIN);
                return returnValue;
            }
        }
    }



    @RequestMapping(value="/del",method=RequestMethod.POST)
    public EmployeeReturnValue delEmployee(@RequestParam("employeeId") Integer employeeId,
                              @RequestParam("ownerId")Integer ownerId,
                              HttpSession session) {
        EmployeeReturnValue returnValue;
        Owner owner = ownerRepository.findOne(ownerId);
        if(owner==null){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_EXIST);
            return returnValue;
        }
        else {
            if (session.getAttribute("ownerSession") == null){
                returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_NOT_LOGIN);
                return returnValue;
            }
            else {
                Owner ownerSession = (Owner) session.getAttribute("ownerSession");
                if (ownerSession.getId().equals(owner.getId())) {
                    Employee employee = employeeRepository.findOne(employeeId);
                    if(employee==null){
                        returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
                        return returnValue;
                    }
                    else {
                        employeeRepository.delete(employee);
                        returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_VALID);
                        returnValue.setEmployee(null);
                        returnValue.setEmployeeList(null);
                        return returnValue;
                    }
                }
                else {
                    returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_LOGIN);
                    return returnValue;
                }
            }
        }
    }

    @RequestMapping(value="/listAll", method=RequestMethod.GET)
    public EmployeeReturnValue listAllEmployee(){
        EmployeeReturnValue returnValue;
        List<Employee> employeeList = (List<Employee>) employeeRepository.findAll();
        if(employeeList==null) {
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEELIST_EMPTY);
            returnValue.setEmployee(null);
            return returnValue;
        }
        else {
            employeeRepository.save(employeeList);
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_VALID);
            returnValue.setEmployeeList(employeeList);
            returnValue.setEmployee(null);
            return returnValue;
        }
    }
    @RequestMapping(value="/listByCompanyId",method=RequestMethod.GET)
    public EmployeeReturnValue listEmployeeByCompanyId(@RequestParam("companyId") Integer companyId){
        EmployeeReturnValue returnValue;
        List<Employee> employeeList = jpaEmployeeRepository.findByCompanyId(companyId);
        if(employeeList==null) {
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEELIST_EMPTY);
            return returnValue;
        }
        else {
            employeeRepository.save(employeeList);
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_VALID);
            returnValue.setEmployeeList(employeeList);
            returnValue.setEmployee(null);
            return returnValue;
        }
    }

    @RequestMapping(value="/listByProjectId",method=RequestMethod.GET)
    public EmployeeReturnValue listEmployeeByProjectId(@RequestParam("projectId")Integer projectId){
        EmployeeReturnValue returnValue;
        List<Employee> employeeList = jpaEmployeeRepository.findByProjectId(projectId);
        if(employeeList==null) {
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEELIST_EMPTY);
            return returnValue;
        }
        else {
            employeeRepository.save(employeeList);
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_VALID);
            returnValue.setEmployeeList(employeeList);
            returnValue.setEmployee(null);
            return returnValue;
        }
    }
}
