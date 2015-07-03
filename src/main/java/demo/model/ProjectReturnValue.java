package demo.model;

import java.lang.*;
import java.lang.Object;
import java.util.*;

/**
 * Created by Khang on 02/07/2015.
 */
public class ProjectReturnValue extends BasicReturnValue {
    private Project project;

//    //private List<Project> projectList = new List<Project>() {
//        @Override
//        public int size() {
//            return 0;
//        }
//
//        @Override
//        public boolean isEmpty() {
//            return false;
//        }
//
//        @Override
//        public boolean contains(Object o) {
//            return false;
//        }
//
//        @Override
//        public Iterator<Project> iterator() {
//            return null;
//        }
//
//        @Override
//        public Object[] toArray() {
//            return new Object[0];
//        }
//
//        @Override
//        public <T> T[] toArray(T[] a) {
//            return null;
//        }
//
//        @Override
//        public boolean add(Project project) {
//            return false;
//        }
//
//        @Override
//        public boolean remove(Object o) {
//            return false;
//        }
//
//        @Override
//        public boolean containsAll(Collection<?> c) {
//            return false;
//        }
//
//        @Override
//        public boolean addAll(Collection<? extends Project> c) {
//            return false;
//        }
//
//        @Override
//        public boolean addAll(int index, Collection<? extends Project> c) {
//            return false;
//        }
//
//        @Override
//        public boolean removeAll(Collection<?> c) {
//            return false;
//        }
//
//        @Override
//        public boolean retainAll(Collection<?> c) {
//            return false;
//        }
//
//        @Override
//        public void clear() {
//
//        }
//
//        @Override
//        public Project get(int index) {
//            return null;
//        }
//
//        @Override
//        public Project set(int index, Project element) {
//            return null;
//        }
//
//        @Override
//        public void add(int index, Project element) {
//
//        }
//
//        @Override
//        public Project remove(int index) {
//            return null;
//        }
//
//        @Override
//        public int indexOf(Object o) {
//            return 0;
//        }
//
//        @Override
//        public int lastIndexOf(Object o) {
//            return 0;
//        }
//
//        @Override
//        public ListIterator<Project> listIterator() {
//            return null;
//        }
//
//        @Override
//        public ListIterator<Project> listIterator(int index) {
//            return null;
//        }
//
//        @Override
//        public List<Project> subList(int fromIndex, int toIndex) {
//            return null;
//        }
//    };

    public ProjectReturnValue(){};

    public ProjectReturnValue(Project project,HashMap errorList){
        super(errorList);
        this.project = project;
    }

//    public ProjectReturnValue(List<Project> projectList,HashMap errorList){
//        super(errorList);
//        this.projectList = projectList;
//    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
