package com.example.miczcj.vms.model;

import java.io.Serializable;

public class RecruitStudent  implements Serializable{
	String time;
	String id;
	String name;
	String sex;
	String stuclass;
	String stuid;
	String volid;//注册号
	String phone;
	String phoneshort;
	String note;
	String idcard;
	String date;
	String clothes;
	String experience;
	String language;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
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
	public String getVolid() {
		return volid;
	}
	public void setVolid(String volid) {
		this.volid = volid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhoneshort() {
		return phoneshort;
	}
	public void setPhoneshort(String phoneshort) {
		this.phoneshort = phoneshort;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getClothes() {
		return clothes;
	}
	public void setClothes(String clothes) {
		this.clothes = clothes;
	}
	public String getExperience() {
		return experience;
	}
	public void setExperience(String experience) {
		this.experience = experience;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public RecruitStudent(String time, String id, String name, String sex, String stuclass, String stuid, String volid,
			String phone, String phoneshort, String note, String idcard, String date, String clothes, String experience,
			String language) {
		super();
		this.time = time;
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.stuclass = stuclass;
		this.stuid = stuid;
		this.volid = volid;
		this.phone = phone;
		this.phoneshort = phoneshort;
		this.note = note;
		this.idcard = idcard;
		this.date = date;
		this.clothes = clothes;
		this.experience = experience;
		this.language = language;
	}
	

}
