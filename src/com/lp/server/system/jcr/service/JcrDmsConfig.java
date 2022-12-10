package com.lp.server.system.jcr.service;

public class JcrDmsConfig {
	private String jniRepoName ;
	private String user ;
	private String password ;
	
	public JcrDmsConfig() {
		setupDefaultCredentials();
	}

	public JcrDmsConfig(String jniJcrRepo) {
		jniRepoName = jniJcrRepo ;
		setupDefaultCredentials();
	}
	
	protected void setupDefaultCredentials() {
		user = "Anonymous";
		password = "";		
	}
	
	public String getJniRepoName() {
		return jniRepoName;
	}
	public void setJniRepoName(String jniRepoName) {
		this.jniRepoName = jniRepoName;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
