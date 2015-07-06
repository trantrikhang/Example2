package demo.controller;

import demo.bean.CompanyReturnValue;
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
    public EmployeeReturnValue viewEmployee(@RequestParam("employeeId") String employeeIdInput) {
        EmployeeReturnValue returnValue;
        if(employeeIdInput.isEmpty()){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else {
            int employeeId = Integer.parseInt(employeeIdInput);
            Employee employee = employeeRepository.findOne(employeeId);
            if (employee == null) {
                returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
                return returnValue;
            } else {
                returnValue = new EmployeeReturnValue(employee, GeneralConstant.RESULT_CODE_VALID);
                return returnValue;
            }
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public EmployeeReturnValue addEmployee(@RequestParam("employeeName") String employeeName,
                                           @RequestParam("employeePassword") String employeePassword,
                                           @RequestParam("companyId") String companyIdInput,
                                           @RequestParam("ownerId") String ownerIdInput,
                                           HttpSession session) {
        EmployeeReturnValue returnValue;
        if(companyIdInput.isEmpty()){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_EMPTY);
            return returnValue;
        }
        else
        if(ownerIdInput.isEmpty()){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_OWNERID_EMPTY);
            return returnValue;
        }
        else {
            int ownerId = Integer.parseInt(ownerIdInput);
            int companyId = Integer.parseInt(companyIdInput);
            employeeName = employeeName.trim();
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
                            if (employeeName.isEmpty()) {
                                returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEENAME_EMPTY);
                                return returnValue;
                            } else {
                                Employee employee = new Employee(employeeName, BCrypt.hashpw(employeePassword, salt), companyId);
                                employeeRepository.save(employee);
                                returnValue = new EmployeeReturnValue(employee, GeneralConstant.RESULT_CODE_VALID);
                                return returnValue;
                            }
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
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public EmployeeReturnValue updateEmployee(@RequestParam("employeeId") String employeeIdInput,
                                              @RequestParam("employeePassword") String employeePassword,
                                              @RequestParam("employeeName") String employeeName,
                                              @RequestParam("companyId") String companyIdInput,
                                              HttpSession session) {
        EmployeeReturnValue returnValue;
        if(companyIdInput.isEmpty()){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_EMPTY);
            return returnValue;
        }
        else
        if(employeeIdInput.isEmpty()){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else {
            int companyId = Integer.parseInt(companyIdInput);
            int employeeId = Integer.parseInt(employeeIdInput);
            employeeName = employeeName.trim();
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
                            if (employeeName.isEmpty()) {
                                returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEENAME_EMPTY);
                                return returnValue;
                            } else {
                                employee.setCompanyId(companyId);
                                employeeRepository.save(employee);
                                returnValue = new EmployeeReturnValue(employee, GeneralConstant.RESULT_CODE_VALID);
                                return returnValue;
                            }
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
    }



    @RequestMapping(value="/del",method=RequestMethod.POST)
    public EmployeeReturnValue delEmployee(@RequestParam("employeeId") String employeeIdInput,
                              @RequestParam("ownerId")String ownerIdInput,
                              HttpSession session) {
        EmployeeReturnValue returnValue;
        if(employeeIdInput.isEmpty()){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_EMPTY);
            return returnValue;
        }
        else
        if(ownerIdInput.isEmpty()){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_OWNERID_EMPTY);
            return returnValue;
        }
        else {
            int ownerId = Integer.parseInt(ownerIdInput);
            int employeeId = Integer.parseInt(employeeIdInput);
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
                        Employee employee = employeeRepository.findOne(employeeId);
                        if (employee == null) {
                            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEEID_NOT_EXIST);
                            return returnValue;
                        } else {
                            employeeRepository.delete(employee);
                            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_VALID);
                            return returnValue;
                        }
                    } else {
                        returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_OWNERID_NOT_LOGIN);
                        return returnValue;
                    }
                }
            }
        }
    }

    @RequestMapping(value="/listAll", method=RequestMethod.GET)
    public EmployeeReturnValue listAllEmployee(){
        EmployeeReturnValue returnValue;
        List<Employee> employeeList = (List<Employee>) employeeRepository.findAll();
        if(employeeList==null) {
            returnValue = new EmployeeReturnValue(employeeList,GeneralConstant.RESULT_CODE_EMPLOYEELIST_EMPTY);
            return returnValue;
        }
        else {
            employeeRepository.save(employeeList);
            returnValue = new EmployeeReturnValue(employeeList,GeneralConstant.RESULT_CODE_VALID);
            return returnValue;
        }
    }
    @RequestMapping(value="/listByCompanyId",method=RequestMethod.GET)
    public EmployeeReturnValue listEmployeeByCompanyId(@RequestParam("companyId") String companyIdInput){
        EmployeeReturnValue returnValue;
        if(companyIdInput.isEmpty()){
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_COMPANYID_EMPTY);
            return returnValue;
        }
        else {
            int companyId = Integer.parseInt(companyIdInput);
            List<Employee> employeeList = jpaEmployeeRepository.findByCompanyId(companyId);
            if (employeeList == null) {
                returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEELIST_EMPTY);
                return returnValue;
            } else {
                employeeRepository.save(employeeList);
                returnValue = new EmployeeReturnValue(employeeList, GeneralConstant.RESULT_CODE_VALID);
                return returnValue;
            }
        }
    }

    @RequestMapping(value="/listByProjectId",method=RequestMethod.GET)
    public EmployeeReturnValue listEmployeeByProjectId(@RequestParam("projectId")String projectIdInput) {
        EmployeeReturnValue returnValue;
        if (projectIdInput.isEmpty()) {
            returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_PROJECTID_EMPTY);
            return returnValue;
        } else {
            int projectId = Integer.parseInt(projectIdInput);
            List<Employee> employeeList = jpaEmployeeRepository.findByProjectId(projectId);
            if (employeeList == null) {
                returnValue = new EmployeeReturnValue(GeneralConstant.RESULT_CODE_EMPLOYEELIST_EMPTY);
                return returnValue;
            } else {
                employeeRepository.save(employeeList);
                returnValue = new EmployeeReturnValue(employeeList, GeneralConstant.RESULT_CODE_VALID);
                return returnValue;
            }
        }
    }
}
