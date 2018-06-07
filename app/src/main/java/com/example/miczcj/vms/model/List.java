package com.example.miczcj.vms.model;

public class List {
	String time;
	String name;
	String num;
	String dcb;
	String dept;
	String type;
	public List(String time, String name, String num, String dcb, String dept, String type) {
		super();
		this.time = time;
		this.name = name;
		this.num = num;
		this.dcb = dcb;
		this.dept = dept;
		this.type = type;
	}
	public List() {
		super();
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getDcb() {
		return dcb;
	}
	public void setDcb(String dcb) {
		this.dcb = dcb;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
