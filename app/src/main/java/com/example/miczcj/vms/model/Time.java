package com.example.miczcj.vms.model;

import java.io.Serializable;

public class Time implements Serializable{
	String name;
	String stuclass;
	String stuid;
	String worknum;
	String activity;
	String time;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStuclass() {
		return stuclass;
	}
	public void setStuclass(String stuclass) {
		this.stuclass = stuclass;
	}
	public String getStuid() {
		return stuid;
	}
	public void setStuid(String stuid) {
		this.stuid = stuid;
	}
	public String getWorknum() {
		return worknum;
	}
	public void setWorknum(String worknum) {
		this.worknum = worknum;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Time(String name, String stuclass, String stuid, String worknum, String activity, String time) {
		super();
		this.name = name;
		this.stuclass = stuclass;
		this.stuid = stuid;
		this.worknum = worknum;
		this.activity = activity;
		this.time = time;
	}

}
