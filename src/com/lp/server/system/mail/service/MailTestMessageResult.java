package com.lp.server.system.mail.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MailTestMessageResult implements Serializable {
	private static final long serialVersionUID = 7186764117816944301L;

	private MailTestMessage mailTestMessage;
	private Properties mailProperties;
	
	private boolean mailCreated = false;
	private boolean mailSent = false;
	private boolean hasImap = false;
	private boolean imapConnected = false;
	private boolean mailStored = false;
	
	private Throwable ex;
	
	private List<String> imapFolders = new ArrayList<String>();

	public MailTestMessageResult(MailTestMessage mailTestMessage, Properties props) {
		this.mailTestMessage = mailTestMessage;
		this.mailProperties = props;
	}
	
	public void mailCreated() {
		mailCreated = true;
	}
	
	public void mailCreationFailed(Throwable ex) {
		mailCreated = false;
		this.ex = ex;
	}

	public void mailSent() {
		mailSent = true;
	}
	public void sendingMailFailed(Throwable ex) {
		mailSent = false;
		this.ex = ex;
	}
	
	public void imapExists() {
		hasImap = true;
	}
	public void imapConnected() {
		imapConnected = true;
	}
	public void imapConnectionFailed(Throwable ex) {
		imapConnected = false;
		this.ex = ex;
	}
	
	public void mailStored() {
		mailStored = true;
	}
	public void mailStorageFailed(Throwable ex, List<String> imapFolders) {
		mailStored = false;
		this.ex = ex;
		this.imapFolders = imapFolders;
	}

	public MailTestMessage getMailTestMessage() {
		return mailTestMessage;
	}

	public boolean isMailCreated() {
		return mailCreated;
	}

	public boolean isMailSent() {
		return mailSent;
	}

	public boolean hasImap() {
		return hasImap;
	}

	public boolean isImapConnected() {
		return imapConnected;
	}

	public boolean isMailStored() {
		return mailStored;
	}

	public Throwable getEx() {
		return ex;
	}

	public List<String> getImapFolders() {
		return imapFolders;
	}
	
	public Properties getMailProperties() {
		return mailProperties;
	}
	
	public boolean hasException() {
		return getEx() != null;
	}
}
