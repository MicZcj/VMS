package com.example.miczcj.vms.model;

import java.io.Serializable;

public class VolunteerActivity implements Serializable{
    String num;
    String id;
    String title;
    String time;
    String deptname;
    String address;
    String actstarttime;
    String actendtime;
    String content;
    String filename;
    String flag;
    String stauts;
    public String getNum() {
        return num;
    }
    public void setNum(String num) {
        this.num = num;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getDeptname() {
        return deptname;
    }
    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getActstarttime() {
        return actstarttime;
    }
    public void setActstarttime(String actstarttime) {
        this.actstarttime = actstarttime;
    }
    public String getActendtime() {
        return actendtime;
    }
    public void setActendtime(String actendtime) {
        this.actendtime = actendtime;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public String getFlag() {
        return flag;
    }
    public void setFlag(String flag) {
        this.flag = flag;
    }
    public String getStauts() {
        return stauts;
    }
    public void setStauts(String stauts) {
        this.stauts = stauts;
    }
    public VolunteerActivity(String num, String id, String title, String time, String deptname, String address,
                              String actstarttime, String actendtime, String content, String filename, String flag, String stauts) {
        super();
        this.num = num;
        this.id = id;
        this.title = title;
        this.time = time;
        this.deptname = deptname;
        this.address = address;
        this.actstarttime = actstarttime;
        this.actendtime = actendtime;
        this.content = content;
        this.filename = filename;
        this.flag = flag;
        this.stauts = stauts;
    }
    public VolunteerActivity(VolunteerActivity activity) {
        super();
        this.num = activity.num;
        this.id = activity.id;
        this.title = activity.title;
        this.time = activity.time;
        this.deptname = activity.deptname;
        this.address = activity.address;
        this.actstarttime = activity.actstarttime;
        this.actendtime = activity.actendtime;
        this.content = activity.content;
        this.filename = activity.filename;
        this.flag = activity.flag;
        this.stauts = activity.stauts;
    }


}
