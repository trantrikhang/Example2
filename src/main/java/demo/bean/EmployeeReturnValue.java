package demo.bean;

import demo.model.Employee;

import java.lang.*;
import java.lang.Object;
import java.util.*;

/**
 * Created by Khang on 02/07/2015.
 */
public class EmployeeReturnValue extends BasicReturnValue {
    private Employee employee;

    private List<Employee> employeeList = new List<Employee>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<Employee> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(Employee employee) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Employee> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends Employee> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Employee get(int index) {
            return null;
        }

        @Override
        public Employee set(int index, Employee element) {
            return null;
        }

        @Override
        public void add(int index, Employee element) {

        }

        @Override
        public Employee remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<Employee> listIterator() {
            return null;
        }

        @Override
        public ListIterator<Employee> listIterator(int index) {
            return null;
        }

        @Override
        public List<Employee> subList(int fromIndex, int toIndex) {
            return null;
        }
    };

    public EmployeeReturnValue(){};

    public EmployeeReturnValue(int errorList){
        super(errorList);
        this.setEmployee(employee);
        this.setEmployeeList(employeeList);
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}
