package com.lp.server.partner.service;

import java.io.Serializable;

public class MaileSerienbriefResponse implements Serializable {
	private static final long serialVersionUID = 1613400193411630559L;

	private int totalCount ;
	private int emailCount ;
	
	public MaileSerienbriefResponse() {
	}
	
	public MaileSerienbriefResponse(int total) {
		setTotalCount(total);
	}
	
	public MaileSerienbriefResponse(int total, int email) {
		setTotalCount(total);
		setEmailCount(email);
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public int getEmailCount() {
		return emailCount;
	}
	public void setEmailCount(int emailCount) {
		this.emailCount = emailCount;
	}	
}
