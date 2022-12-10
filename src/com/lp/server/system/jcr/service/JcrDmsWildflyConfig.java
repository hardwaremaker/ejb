package com.lp.server.system.jcr.service;

public class JcrDmsWildflyConfig extends JcrDmsConfig {
	public JcrDmsWildflyConfig(String jniJcrRepo) {
		super(jniJcrRepo);
	}
	
	@Override
	protected void setupDefaultCredentials() {
		setUser("jcr");
		setPassword("jcr");
	}
}
