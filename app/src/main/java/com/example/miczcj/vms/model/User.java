package com.example.miczcj.vms.model;

import java.io.Serializable;

public class User implements Serializable{
	int uid;
	String username;
	String dept;
	String password;
	String authority;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public User() {
		super();
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public User(int uid, String username, String dept, String password, String authority) {
		super();
		this.uid = uid;
		this.username = username;
		this.dept = dept;
		this.password = password;
		this.authority = authority;
	}

	
}
