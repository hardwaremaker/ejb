package com.lp.server.forecast.service;

import java.io.Serializable;
import java.util.List;

import com.lp.util.EJBLineNumberExceptionLP;

public class CallOffXlsImporterResult implements Serializable {

	private static final long serialVersionUID = 3528136721923248602L;

	private List<EJBLineNumberExceptionLP> messages ;
	private CallOffXlsImportStats stats;
	
	public CallOffXlsImporterResult(List<EJBLineNumberExceptionLP> messages, CallOffXlsImportStats stats) {
		setMessages(messages);
		setStats(stats);
	}

	public List<EJBLineNumberExceptionLP> getMessages() {
		return messages;
	}

	public void setMessages(List<EJBLineNumberExceptionLP> messages) {
		this.messages = messages;
	}

	public CallOffXlsImportStats getStats() {
		return stats;
	}

	public void setStats(CallOffXlsImportStats stats) {
		this.stats = stats;
	}

}
