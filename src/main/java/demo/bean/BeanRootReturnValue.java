package demo.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Khang on 02/07/2015.
 */
public abstract class BeanRootReturnValue implements Serializable{
    private int resultCode;

    public String resultMessage;

    public HashMap hashMapCodeMessage;

    public Long timeStamp;

    public BeanRootReturnValue(){};

    Map<Integer,String> hashMap = new HashMap<Integer,String>() {
        {
            put(0, "SUCCESS");
            put(-100, "ERROR");
            put(-101, "NOT LOGIN");
            put(-102, "WRONG PASSWORD");
            put(-103, "PASSWORD EMPTY");
            put(-200, "OWNERID NOT EXIST");
            put(-201, "OWNER ONLY OWN 1 COMPANY");
            put(-202, "OWNERID NOT LOGIN");
            put(-203, "OWNERID NOT OWN THIS COMPANY");
            put(-204, "OWNERID EXISTED");
            put(-205, "OWNERNAME EMPTY");
            put(-206, "OWNERPASSWORD EMPTY");
            put(-207, "OWNERID EMPTY");
            put(-300, "COMPANYID NOT EXIST");
            put(-301, "COMPANYID EXISTED");
            put(-302, "COMPANYLIST EMPTY");
            put(-303, "COMPANYNAME EMPTY");
            put(-304, "COMPANYID EMPTY");
            put(-400, "PROJECTLIST EMPTY");
            put(-401, "PROJECTID EXISTED");
            put(-402, "PROJECTMANAGERID NOT EXIST");
            put(-403, "PROJECTID NOT EXIST");
            put(-404, "PROJECTNAME EMPTY");
            put(-405, "PROJECTID EMPTY");
            put(-500, "TASKCHILDLIST EMPTY");
            put(-501, "TASKLIST EMPTY");
            put(-502, "TASKID EXISTED");
            put(-503, "TASKPARENTID EXISTED");
            put(-504, "TASKCHILDID EXISTED");
            put(-505, "TASKCHILDID NOT EXIST");
            put(-506, "TASKPARENTID NOT EXIST");
            put(-507, "TASKID NOT EXIST");
            put(-508, "TASKNAME EMPTY");
            put(-509, "TASKID EMPTY");
            put(-600, "EMPLOYEELIST EMPTY");
            put(-601, "EMPLOYEEID NOT EXIST");
            put(-602, "EMPLOYEEID EXISTED");
            put(-603, "EMPLOYEES PROJECTID EXISTED");
            put(-604, "MANAGERID NOT LOGIN");
            put(-605, "MANAGERID NOT BELONG TO COMPANY");
            put(-606, "EMPLOYEENAME EMPTY");
            put(-607, "EMPLOYEEID EMPTY");
        }
    };

    public BeanRootReturnValue(int resultCode){
        this.resultCode = resultCode;
        this.resultMessage = (String)hashMap.get(resultCode);
        this.setTimeStamp(System.currentTimeMillis());
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public HashMap getHashMapCodeMessage() {
        return hashMapCodeMessage;
    }

    public void setHashMapCodeMessage(HashMap hashMapCodeMessage) {
        this.hashMapCodeMessage = hashMapCodeMessage;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
