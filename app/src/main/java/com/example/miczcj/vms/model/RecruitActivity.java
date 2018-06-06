package com.example.miczcj.vms.model;

import java.io.Serializable;

public class RecruitActivity  implements Serializable{
	String id;
	String name;
	String dept;
	String address;
	String time;
	String num;
	String content;
	String phone;
	String flag;
	String selecttime;
	String have;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getHave() {
		return have;
	}
	public void setHave(String have) {
		this.have = have;
	}
	public String getSelecttime() {
		return selecttime;
	}
	public void setSelecttime(String selecttime) {
		this.selecttime = selecttime;
	}
	public RecruitActivity(String id, String name, String dept, String address, String time, String num, String content,
			String phone, String flag, String selecttime, String have) {
		super();
		this.id = id;
		this.name = name;
		this.dept = dept;
		this.address = address;
		this.time = time;
		this.num = num;
		this.content = content;
		this.phone = phone;
		this.flag = flag;
		this.selecttime = selecttime;
		this.have = have;
	}





}
